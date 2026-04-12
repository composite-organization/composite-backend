package kr.composite.api.vote.application;

import java.util.List;
import kr.composite.api.student.domain.StudentRepository;
import kr.composite.api.student.domain.Students;
import kr.composite.api.vote.domain.VoteOption;
import kr.composite.api.vote.domain.VoteOptionContent;
import kr.composite.api.vote.domain.VoteOptionRepository;
import kr.composite.api.vote.domain.VoteStatus;
import kr.composite.api.vote.domain.VoteSubmissionRepository;
import kr.composite.api.vote.domain.VoteSubmissions;
import kr.composite.api.vote.domain.VoteTitle;
import kr.composite.api.vote.domain.VoteWidget;
import kr.composite.api.vote.domain.VoteWidgetRepository;
import kr.composite.api.vote.ui.dto.request.PostVoteSubmissionRequest;
import kr.composite.api.vote.ui.dto.request.PostVoteWidgetRequest;
import kr.composite.api.vote.ui.dto.response.CreateVoteWidgetResponse;
import kr.composite.api.vote.ui.dto.response.GetVoteWidgetResponse;
import kr.composite.api.vote.ui.dto.response.VoteEndedData;
import kr.composite.api.vote.ui.dto.response.VoteInProgressData;
import kr.composite.api.vote.ui.dto.response.VoteStatusData;
import kr.composite.api.widget.domain.Widget;
import kr.composite.api.widget.domain.WidgetRepository;
import kr.composite.api.widget.domain.WidgetType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VoteWidgetService {

    private final VoteWidgetRepository voteWidgetRepository;
    private final VoteOptionRepository voteOptionRepository;
    private final VoteSubmissionRepository voteSubmissionRepository;
    private final WidgetRepository widgetRepository;
    private final StudentRepository studentRepository;

    @Transactional
    public CreateVoteWidgetResponse addVoteWidget(PostVoteWidgetRequest request) {
        if (request.options().isEmpty()) {
            throw VoteApplicationException.emptyOptions();
        }

        Widget widget = new Widget(request.lessonId(), WidgetType.VOTE);
        widgetRepository.save(widget);

        VoteTitle voteTitle = new VoteTitle(request.title());
        VoteWidget voteWidget = new VoteWidget(
                widget.getId(),
                voteTitle,
                request.isAnonymous(),
                request.isMultiSelectable()
        );
        voteWidgetRepository.save(voteWidget);

        List<VoteOption> voteOptions = request.options().stream()
                .map(optionContent -> new VoteOption(voteWidget.getId(), new VoteOptionContent(optionContent)))
                .toList();
        List<VoteOption> savedOptions = voteOptionRepository.saveAll(voteOptions);

        return CreateVoteWidgetResponse.of(voteWidget, savedOptions);
    }

    public GetVoteWidgetResponse findVoteWidget(Long voteWidgetId) {
        VoteWidget voteWidget = voteWidgetRepository.findById(voteWidgetId)
                .orElseThrow(VoteApplicationException::widgetNotFound);

        List<VoteOption> voteOptions = voteOptionRepository.findAllByVoteWidgetId(voteWidgetId);

        VoteStatusData data = switch (voteWidget.getVoteStatus()) {
            case IN_PROGRESS -> buildInProgressData(voteWidget, voteOptions);
            case ENDED -> buildEndedData(voteWidgetId, voteOptions);
        };

        return GetVoteWidgetResponse.of(voteWidget, voteOptions, data);
    }

    @Transactional
    public void updateVoteStatus(Long voteWidgetId, VoteStatus voteStatus) {
        VoteWidget voteWidget = voteWidgetRepository.findById(voteWidgetId)
                .orElseThrow(VoteApplicationException::widgetNotFound);

        voteWidget.changeStatus(voteStatus);
    }

    @Transactional
    public void submitVote(Long voteWidgetId, PostVoteSubmissionRequest request) {
        VoteWidget voteWidget = voteWidgetRepository.findById(voteWidgetId)
                .orElseThrow(VoteApplicationException::widgetNotFound);

        if (voteWidget.getVoteStatus() != VoteStatus.IN_PROGRESS) {
            throw VoteApplicationException.voteNotInProgress();
        }

        if (request.hasEmptyOptionIds()) {
            throw VoteApplicationException.emptyOptionIds();
        }

        if (!voteWidget.isMultiSelectable() && request.hasMultipleOptionIds()) {
            throw VoteApplicationException.multipleOptionsNotAllowed();
        }

        if (voteSubmissionRepository.existsByStudentIdAndVoteWidgetId(request.studentId(), voteWidgetId)) {
            throw VoteApplicationException.alreadySubmitted();
        }

        if (!voteOptionRepository.existsAllByIdInAndVoteWidgetId(request.optionIds(), voteWidgetId)) {
            throw VoteApplicationException.invalidOptionForVote();
        }
        voteSubmissionRepository.saveAll(request.toVoteSubmissions(voteWidgetId));
    }

    @Transactional
    public void deleteVoteWidget(Long voteWidgetId) {
        VoteWidget voteWidget = voteWidgetRepository.findById(voteWidgetId).orElse(null);
        if (voteWidget == null) {
            return;
        }
        voteSubmissionRepository.deleteAllByVoteWidgetId(voteWidgetId);
        voteOptionRepository.deleteAllByVoteWidgetId(voteWidgetId);
        voteWidgetRepository.deleteById(voteWidgetId);
        widgetRepository.deleteById(voteWidget.getWidgetId());
    }

    private VoteInProgressData buildInProgressData(VoteWidget voteWidget, List<VoteOption> voteOptions) {
        VoteSubmissions voteSubmissions = new VoteSubmissions(
                voteSubmissionRepository.findAllByVoteWidgetId(voteWidget.getId()));

        if (voteWidget.isAnonymous()) {
            return VoteInProgressData.anonymous(voteOptions, voteSubmissions);
        }
        Students students = studentRepository.findAllByIdIn(voteSubmissions.getDistinctStudentIds());

        return VoteInProgressData.identified(voteOptions, voteSubmissions, students);
    }

    private VoteEndedData buildEndedData(Long voteWidgetId, List<VoteOption> voteOptions) {
        VoteSubmissions voteSubmissions = new VoteSubmissions(
                voteSubmissionRepository.findAllByVoteWidgetId(voteWidgetId));

        return VoteEndedData.of(voteOptions, voteSubmissions);
    }
}
