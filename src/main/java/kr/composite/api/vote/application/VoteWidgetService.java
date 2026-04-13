package kr.composite.api.vote.application;

import java.util.List;
import kr.composite.api.student.domain.Student;
import kr.composite.api.student.domain.StudentRepository;
import kr.composite.api.teacher.domain.TeacherRepository;
import kr.composite.api.user.domain.User;
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
    private final TeacherRepository teacherRepository;

    @Transactional
    public CreateVoteWidgetResponse addVoteWidget(User user, PostVoteWidgetRequest request) {
        if (request.options().isEmpty()) {
            throw VoteApplicationException.emptyOptions();
        }
        if (!isTeacherOfLesson(user, request.lessonId())) {
            throw VoteApplicationException.forbidden();
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
        List<VoteOption> voteOptions = voteOptionRepository.saveAll(request.toVoteOptions(voteWidget.getId()));

        return CreateVoteWidgetResponse.of(voteWidget, voteOptions);
    }

    public GetVoteWidgetResponse readVoteWidget(User user, Long voteWidgetId) {
        VoteWidget voteWidget = voteWidgetRepository.findById(voteWidgetId)
                .orElseThrow(VoteApplicationException::voteWidgetNotFound);
        Widget widget = widgetRepository.findById(voteWidget.getWidgetId())
                .orElseThrow(VoteApplicationException::voteWidgetNotFound);
        if (!isTeacherOfLesson(user, widget.getLessonId()) && !isStudentOfLesson(user, widget.getLessonId())) {
            throw VoteApplicationException.forbidden();
        }
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
    public void updateVoteStatus(User user, Long voteWidgetId, VoteStatus voteStatus) {
        VoteWidget voteWidget = voteWidgetRepository.findById(voteWidgetId)
                .orElseThrow(VoteApplicationException::voteWidgetNotFound);
        Widget widget = widgetRepository.findById(voteWidget.getWidgetId())
                .orElseThrow(VoteApplicationException::voteWidgetNotFound);
        if (!isTeacherOfLesson(user, widget.getLessonId())) {
            throw VoteApplicationException.forbidden();
        }
        voteWidget.changeStatus(voteStatus);
    }

    @Transactional
    public void submitVote(User user, Long voteWidgetId, PostVoteSubmissionRequest request) {
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
        Widget widget = widgetRepository.findById(voteWidget.getWidgetId())
                .orElseThrow(VoteApplicationException::voteWidgetNotFound);
        Student student = studentRepository.findByLessonIdAndUserId(widget.getLessonId(), user.getId())
                .orElseThrow(VoteApplicationException::forbidden);
        if (!voteOptionRepository.existsAllByIdInAndVoteWidgetId(request.optionIds(), voteWidget.getId())) {
            throw VoteApplicationException.invalidOptionForVote();
        }
        if (voteSubmissionRepository.existsByStudentIdAndVoteWidgetId(student.getId(), voteWidget.getId())) {
            throw VoteApplicationException.alreadySubmitted();
        }
        voteSubmissionRepository.saveAll(request.toVoteSubmissions(voteWidgetId, student.getId()));
    }

    @Transactional
    public void deleteVoteWidget(User user, Long voteWidgetId) {
        VoteWidget voteWidget = voteWidgetRepository.findById(voteWidgetId).orElse(null);
        if (voteWidget == null) {
            return;
        }
        Widget widget = widgetRepository.findById(voteWidget.getWidgetId())
                .orElseThrow(VoteApplicationException::voteWidgetNotFound);
        if (!isTeacherOfLesson(user, widget.getLessonId())) {
            throw VoteApplicationException.forbidden();
        }
        voteSubmissionRepository.deleteAllByVoteWidgetId(voteWidgetId);
        voteOptionRepository.deleteAllByVoteWidgetId(voteWidgetId);
        voteWidgetRepository.deleteById(voteWidgetId);
        widgetRepository.deleteById(widget.getId());
    }

    private boolean isTeacherOfLesson(User user, Long lessonId) {
        return teacherRepository.existsByLessonIdAndUserId(lessonId, user.getId());
    }

    private boolean isStudentOfLesson(User user, Long lessonId) {
        return studentRepository.existsByLessonIdAndUserId(lessonId, user.getId());
    }
}
