package kr.composite.api.vote.application;

import java.util.List;
import kr.composite.api.student.domain.StudentRepository;
import kr.composite.api.vote.domain.VoteOption;
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
import kr.composite.api.vote.ui.dto.response.VoteParticipationResponse;
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
        List<VoteOption> voteOptions = voteOptionRepository.saveAll(request.toVoteOptions(voteWidget.getId()));

        return CreateVoteWidgetResponse.of(voteWidget, voteOptions);
    }

    public GetVoteWidgetResponse findVoteWidget(Long voteWidgetId) {
        VoteWidget voteWidget = voteWidgetRepository.findById(voteWidgetId)
                .orElseThrow(VoteApplicationException::voteWidgetNotFound);
        VoteSubmissions voteSubmissions = new VoteSubmissions(
                voteOptionRepository.findAllByVoteWidgetId(voteWidgetId),
                voteSubmissionRepository.findAllByVoteWidgetId(voteWidget.getId())
        );
        VoteParticipationResponse participationResponse = voteWidget.isAnonymous()
                ? VoteParticipationResponse.anonymous(voteSubmissions)
                : VoteParticipationResponse.identified(
                        voteSubmissions,
                        studentRepository.findAllByIdIn(voteSubmissions.getDistinctStudentIds())
                );
        return GetVoteWidgetResponse.of(voteWidget, voteSubmissions, participationResponse);
    }

    @Transactional
    public void updateVoteStatus(Long voteWidgetId, VoteStatus voteStatus) {
        VoteWidget voteWidget = voteWidgetRepository.findById(voteWidgetId)
                .orElseThrow(VoteApplicationException::voteWidgetNotFound);

        voteWidget.changeStatus(voteStatus);
    }

    @Transactional
    public void submitVote(Long voteWidgetId, PostVoteSubmissionRequest request) {
        if (request.hasEmptyOptionIds()) {
            throw VoteApplicationException.emptyOptionIds();
        }
        VoteWidget voteWidget = voteWidgetRepository.findById(voteWidgetId)
                .orElseThrow(VoteApplicationException::voteWidgetNotFound);
        if (!voteWidget.isMultiSelectable() && request.hasMultipleOptionIds()) {
            throw VoteApplicationException.multipleOptionsNotAllowed();
        }
        if (voteWidget.getVoteStatus() != VoteStatus.IN_PROGRESS) {
            throw VoteApplicationException.voteNotInProgress();
        }
        if (!voteOptionRepository.existsAllByIdInAndVoteWidgetId(request.optionIds(), voteWidget.getId())) {
            throw VoteApplicationException.invalidOptionForVote();
        }
        if (voteSubmissionRepository.existsByStudentIdAndVoteWidgetId(request.studentId(), voteWidget.getId())) {
            throw VoteApplicationException.alreadySubmitted();
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
}
