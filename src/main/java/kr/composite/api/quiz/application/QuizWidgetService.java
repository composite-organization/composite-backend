package kr.composite.api.quiz.application;

import java.util.List;
import java.util.Objects;
import kr.composite.api.quiz.domain.QuizOption;
import kr.composite.api.quiz.domain.QuizOptionRepository;
import kr.composite.api.quiz.domain.QuizStatus;
import kr.composite.api.quiz.domain.QuizSubmissionRepository;
import kr.composite.api.quiz.domain.QuizSubmissions;
import kr.composite.api.quiz.domain.QuizTitle;
import kr.composite.api.quiz.domain.QuizWidget;
import kr.composite.api.quiz.domain.QuizWidgetRepository;
import kr.composite.api.quiz.ui.dto.request.CreateQuizWidgetRequest;
import kr.composite.api.quiz.ui.dto.request.SubmitQuizSubmissionRequest;
import kr.composite.api.quiz.ui.dto.request.UpdateQuizOptionRequest;
import kr.composite.api.quiz.ui.dto.request.UpdateQuizOptionRequest.QuizOptionRequest;
import kr.composite.api.quiz.ui.dto.request.UpdateQuizWidgetStatusRequest;
import kr.composite.api.quiz.ui.dto.response.CreateQuizWidgetResponse;
import kr.composite.api.quiz.ui.dto.response.GetQuizAnswerResponse;
import kr.composite.api.quiz.ui.dto.response.GetQuizWidgetResponse;
import kr.composite.api.quiz.ui.dto.response.QuizSubmissionSummaryResponse;
import kr.composite.api.quiz.ui.dto.response.UpdateQuizOptionResponse;
import kr.composite.api.student.domain.Student;
import kr.composite.api.student.domain.StudentRepository;
import kr.composite.api.student.domain.Students;
import kr.composite.api.teacher.domain.TeacherRepository;
import kr.composite.api.user.domain.User;
import kr.composite.api.widget.domain.Widget;
import kr.composite.api.widget.domain.WidgetRepository;
import kr.composite.api.widget.domain.WidgetType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QuizWidgetService {

    private final QuizWidgetRepository quizWidgetRepository;
    private final WidgetRepository widgetRepository;
    private final QuizOptionRepository quizOptionRepository;
    private final QuizSubmissionRepository quizSubmissionRepository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;

    @Transactional
    public CreateQuizWidgetResponse addQuizWidget(User user, CreateQuizWidgetRequest request) {
        if (!isTeacherOfLesson(user.getId(), request.lessonId())) {
            throw QuizWidgetApplicationException.forbidden();
        }

        Widget widget = new Widget(request.lessonId(), WidgetType.QUIZ);
        widgetRepository.save(widget);

        QuizWidget quizWidget = new QuizWidget(widget.getId(), new QuizTitle(request.title()));
        quizWidgetRepository.save(quizWidget);

        quizOptionRepository.saveAll(request.toQuizOptions(quizWidget.getId()));

        return CreateQuizWidgetResponse.from(quizWidget.getId());
    }

    public GetQuizWidgetResponse getQuizWidget(User user, Long quizWidgetId) {
        QuizWidget quizWidget = getQuizWidget(quizWidgetId);
        Widget widget = getWidget(quizWidget.getWidgetId());

        validateAccess(user.getId(), widget.getLessonId());

        QuizSubmissions quizSubmissions = new QuizSubmissions(
                quizOptionRepository.findAllByQuizWidgetId(quizWidgetId),
                quizSubmissionRepository.findAllByQuizWidgetId(quizWidgetId)
        );

        List<Long> studentSubmittedOptionIds = getStudentSubmittedOptionIds(
                user,
                widget.getLessonId(),
                quizSubmissions
        );

        Students students = studentRepository.findAllByIdIn(quizSubmissions.getDistinctStudentIds());
        QuizSubmissionSummaryResponse submissionSummary = QuizSubmissionSummaryResponse.of(quizSubmissions, students);

        return GetQuizWidgetResponse.of(quizWidget, quizSubmissions, studentSubmittedOptionIds, submissionSummary);
    }

    private List<Long> getStudentSubmittedOptionIds(User user, Long lessonId, QuizSubmissions quizSubmissions) {
        return studentRepository.findByLessonIdAndUserId(lessonId, user.getId())
                .map(student -> quizSubmissions.getSubmittedOptionIdsByStudentId(student.getId()))
                .orElse(List.of());
    }

    @Transactional
    public void updateQuizWidgetStatus(User user, Long quizWidgetId, UpdateQuizWidgetStatusRequest request) {
        QuizWidget quizWidget = getQuizWidget(quizWidgetId);
        Widget widget = getWidget(quizWidget.getWidgetId());

        if (!isTeacherOfLesson(user.getId(), widget.getLessonId())) {
            throw QuizWidgetApplicationException.forbidden();
        }

        quizWidget.updateStatus(QuizStatus.fromDescription(request.status()));
    }

    @Transactional
    public void deleteQuizWidget(User user, Long quizWidgetId) {
        QuizWidget quizWidget = getQuizWidget(quizWidgetId);
        Widget widget = getWidget(quizWidget.getWidgetId());

        if (!isTeacherOfLesson(user.getId(), widget.getLessonId())) {
            throw QuizWidgetApplicationException.forbidden();
        }

        quizSubmissionRepository.deleteAllByQuizWidgetId(quizWidgetId);
        quizOptionRepository.deleteAllByQuizWidgetId(quizWidgetId);
        quizWidgetRepository.deleteById(quizWidgetId);
        widgetRepository.deleteById(widget.getId());
    }

    @Transactional
    public void submitQuizSubmission(User user, Long quizWidgetId, SubmitQuizSubmissionRequest request) {
        QuizWidget quizWidget = getQuizWidget(quizWidgetId);

        if (quizWidget.getQuizStatus() != QuizStatus.IN_PROGRESS) {
            throw QuizWidgetApplicationException.notInProgress();
        }

        Widget widget = getWidget(quizWidget.getWidgetId());
        Student student = studentRepository.findByLessonIdAndUserId(widget.getLessonId(), user.getId())
                .orElseThrow(QuizWidgetApplicationException::forbidden);

        if (quizSubmissionRepository.existsByStudentIdAndQuizWidgetId(student.getId(), quizWidgetId)) {
            throw QuizWidgetApplicationException.alreadySubmitted();
        }

        quizSubmissionRepository.saveAll(request.toQuizSubmissions(student.getId(), quizWidgetId));
    }

    public GetQuizAnswerResponse getQuizAnswer(User user, Long quizWidgetId) {
        QuizWidget quizWidget = getQuizWidget(quizWidgetId);
        Widget widget = getWidget(quizWidget.getWidgetId());

        validateAccess(user.getId(), widget.getLessonId());

        QuizSubmissions quizSubmissions = new QuizSubmissions(
                quizOptionRepository.findAllByQuizWidgetId(quizWidgetId),
                List.of()
        );

        return GetQuizAnswerResponse.from(quizSubmissions.getCorrectOptionIds());
    }

    @Transactional
    public UpdateQuizOptionResponse updateQuizOption(User user, UpdateQuizOptionRequest request) {
        Long quizWidgetId = request.quizWidgetId();

        validateOptionUpdate(user, quizWidgetId);
        List<QuizOption> existingOptions = quizOptionRepository.findAllByQuizWidgetId(quizWidgetId);

        deleteRemovedOptions(existingOptions, request.options());

        saveOrUpdateOptions(quizWidgetId, existingOptions, request.options());

        return UpdateQuizOptionResponse.from(quizWidgetId);
    }

    private void validateOptionUpdate(User user, Long quizWidgetId) {
        QuizWidget quizWidget = getQuizWidget(quizWidgetId);
        Widget widget = getWidget(quizWidget.getWidgetId());

        if (!isTeacherOfLesson(user.getId(), widget.getLessonId())) {
            throw QuizWidgetApplicationException.forbidden();
        }

        if (quizSubmissionRepository.existsByQuizWidgetId(quizWidgetId)) {
            throw QuizWidgetApplicationException.alreadyQuizSubmitted();
        }
    }

    private void deleteRemovedOptions(List<QuizOption> existingOptions, List<QuizOptionRequest> requests) {
        List<Long> requestOptionIds = requests.stream()
                .map(QuizOptionRequest::quizOptionId)
                .filter(Objects::nonNull)
                .toList();

        List<QuizOption> toDelete = existingOptions.stream()
                .filter(option -> !requestOptionIds.contains(option.getId()))
                .toList();

        if (!toDelete.isEmpty()) {
            quizOptionRepository.deleteAllInBatch(toDelete);
        }
    }

    private void saveOrUpdateOptions(
            Long quizWidgetId,
            List<QuizOption> existingOptions,
            List<QuizOptionRequest> requests
    ) {
        for (QuizOptionRequest optionRequest : requests) {
            if (optionRequest.quizOptionId() != null) {
                updateExistingOption(existingOptions, optionRequest);
            } else {
                quizOptionRepository.save(
                        new QuizOption(quizWidgetId, optionRequest.content(), optionRequest.isCorrect())
                );
            }
        }
    }

    private void updateExistingOption(List<QuizOption> existingOptions, QuizOptionRequest optionRequest) {
        QuizOption existingOption = existingOptions.stream()
                .filter(option -> option.getId().equals(optionRequest.quizOptionId()))
                .findFirst()
                .orElseThrow(QuizWidgetApplicationException::quizOptionNotFound);

        existingOption.update(optionRequest.content(), optionRequest.isCorrect());
    }

    private QuizWidget getQuizWidget(Long quizWidgetId) {
        return quizWidgetRepository.findById(quizWidgetId)
                .orElseThrow(QuizWidgetApplicationException::quizWidgetNotFound);
    }

    private Widget getWidget(Long widgetId) {
        return widgetRepository.findById(widgetId)
                .orElseThrow(QuizWidgetApplicationException::quizWidgetNotFound);
    }

    private void validateAccess(Long userId, Long lessonId) {
        if (!isTeacherOfLesson(userId, lessonId) && !isStudentOfLesson(userId, lessonId)) {
            throw QuizWidgetApplicationException.forbidden();
        }
    }

    private boolean isTeacherOfLesson(Long userId, Long lessonId) {
        return teacherRepository.existsByLessonIdAndUserId(lessonId, userId);
    }

    private boolean isStudentOfLesson(Long userId, Long lessonId) {
        return studentRepository.existsByLessonIdAndUserId(lessonId, userId);
    }
}
