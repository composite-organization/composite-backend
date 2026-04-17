package kr.composite.api.quiz.application;

import java.util.List;
import java.util.Objects;
import kr.composite.api.quiz.domain.QuizOption;
import kr.composite.api.quiz.domain.QuizOptionRepository;
import kr.composite.api.quiz.domain.QuizStatus;
import kr.composite.api.quiz.domain.QuizSubmission;
import kr.composite.api.quiz.domain.QuizSubmissionRepository;
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
import kr.composite.api.quiz.ui.dto.response.QuizParticipationResponse;
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


        QuizTitle quizTitle = new QuizTitle(request.title());
        QuizWidget quizWidget = new QuizWidget(widget.getId(), quizTitle);
        quizWidgetRepository.save(quizWidget);

        Long quizWidgetId = quizWidget.getId();
        List<QuizOption> quizOptions = request.options().stream()
                .map(option -> new QuizOption(quizWidgetId, option.content(), option.isCorrect()))
                .toList();
        quizOptionRepository.saveAll(quizOptions);

        return CreateQuizWidgetResponse.from(quizWidgetId);
    }


    @Transactional(readOnly = true)
    public GetQuizWidgetResponse readQuizWidget(User user, Long quizWidgetId) {
        QuizWidget quizWidget = quizWidgetRepository.findById(quizWidgetId)
                .orElseThrow(QuizWidgetApplicationException::quizWidgetNotFound);

        Widget widget = widgetRepository.findById(quizWidget.getWidgetId())
                .orElseThrow(QuizWidgetApplicationException::quizWidgetNotFound);

        Long lessonId = widget.getLessonId();
        boolean isTeacher = isTeacherOfLesson(user.getId(), lessonId);
        boolean isStudent = isStudentOfLesson(user.getId(), lessonId);

        if (!isTeacher && !isStudent) {
            throw QuizWidgetApplicationException.forbidden();
        }

        List<QuizOption> quizOptions = quizOptionRepository.findAllByQuizWidgetId(quizWidgetId);

        int correctRate = calculateCorrectRate(user, quizWidgetId);

        List<Long> submittedOptionIds = List.of();
        if (isStudent) {
            Student student = studentRepository.findByLessonIdAndUserId(lessonId, user.getId())
                    .orElseThrow(QuizWidgetApplicationException::forbidden);
            submittedOptionIds = quizSubmissionRepository.findAllByStudentIdAndQuizWidgetId(student.getId(),
                            quizWidgetId)
                    .stream()
                    .map(QuizSubmission::getQuizOptionId)
                    .toList();
        }

        List<QuizSubmission> allSubmissions = quizSubmissionRepository.findAllByQuizWidgetId(quizWidgetId);
        List<Long> studentIds = allSubmissions.stream()
                .map(QuizSubmission::getStudentId)
                .distinct()
                .toList();
        Students students = studentRepository.findAllByIdIn(studentIds);

        QuizParticipationResponse participationResponse = QuizParticipationResponse.of(
                quizOptions,
                allSubmissions,
                students
        );

        return GetQuizWidgetResponse.of(quizWidget, quizOptions, correctRate, submittedOptionIds, participationResponse);
    }

    private int calculateCorrectRate(User user, Long quizWidgetId) {
        Long totalSubmissions = quizSubmissionRepository.countByQuizWidgetId(quizWidgetId);
        if (totalSubmissions == 0) {
            return 0;
        }

        List<Long> correctOptionIds = readQuizAnswer(user, quizWidgetId).answerQuizOptionIds();

        if (correctOptionIds.isEmpty()) {
            return 0;
        }

        Long correctSubmissions = quizSubmissionRepository.countByQuizWidgetIdAndQuizOptionIdIn(quizWidgetId,
                correctOptionIds);

        double rawRate = (double) correctSubmissions / totalSubmissions * 100;
        return (int) Math.round(rawRate);
    }

    @Transactional
    public void updateQuizWidgetStatus(User user, Long quizWidgetId, UpdateQuizWidgetStatusRequest request) {
        QuizWidget quizWidget = quizWidgetRepository.findById(quizWidgetId)
                .orElseThrow(QuizWidgetApplicationException::quizWidgetNotFound);
        quizWidget.updateStatus(QuizStatus.fromDescription(request.status()));

        Widget widget = widgetRepository.findById(quizWidget.getWidgetId())
                .orElseThrow(QuizWidgetApplicationException::quizWidgetNotFound);

        if (!isTeacherOfLesson(user.getId(), widget.getLessonId())) {
            throw QuizWidgetApplicationException.forbidden();
        }

        quizWidgetRepository.save(quizWidget);
    }

    @Transactional
    public void deleteQuizWidget(User user, Long quizWidgetId) {
        QuizWidget quizWidget = quizWidgetRepository.findById(quizWidgetId)
                .orElseThrow(QuizWidgetApplicationException::quizWidgetNotFound);

        Widget widget = widgetRepository.findById(quizWidget.getWidgetId())
                .orElseThrow(QuizWidgetApplicationException::quizWidgetNotFound);

        if (!isTeacherOfLesson(user.getId(), widget.getLessonId())) {
            throw QuizWidgetApplicationException.forbidden();
        }

        quizSubmissionRepository.deleteAllByQuizWidgetId(quizWidgetId);
        quizOptionRepository.deleteAllByQuizWidgetId(quizWidgetId);
        quizWidgetRepository.deleteById(quizWidgetId);
    }

    @Transactional
    public void submitQuizSubmission(User user, Long quizWidgetId, SubmitQuizSubmissionRequest request) {
        QuizWidget quizWidget = quizWidgetRepository.findById(quizWidgetId)
                .orElseThrow(QuizWidgetApplicationException::quizWidgetNotFound);

        if (!quizWidget.getQuizStatus().equals(QuizStatus.IN_PROGRESS)) {
            throw QuizWidgetApplicationException.notInProgress();
        }

        Widget widget = widgetRepository.findById(quizWidget.getWidgetId())
                .orElseThrow(QuizWidgetApplicationException::quizWidgetNotFound);

        if (!isTeacherOfLesson(user.getId(), widget.getLessonId()) && !isStudentOfLesson(user.getId(),
                widget.getLessonId())) {
            throw QuizWidgetApplicationException.forbidden();
        }

        Student student = studentRepository.findByUserId(user.getId())
                .orElseThrow(QuizWidgetApplicationException::forbidden);

        if (quizSubmissionRepository.existsByStudentIdAndQuizWidgetId(student.getId(), quizWidgetId)) {
            throw QuizWidgetApplicationException.alreadySubmitted();
        }

        List<QuizSubmission> submissions = request.quizOptionIds().stream()
                .map(optionId -> new QuizSubmission(student.getId(), quizWidgetId, optionId))
                .toList();

        quizSubmissionRepository.saveAll(submissions);
    }

    @Transactional(readOnly = true)
    public GetQuizAnswerResponse readQuizAnswer(User user, Long quizWidgetId) {
        List<QuizOption> quizOptions = quizOptionRepository.findAllByQuizWidgetIdAndIsCorrectTrue(quizWidgetId);

        QuizWidget quizWidget = quizWidgetRepository.findById(quizWidgetId)
                .orElseThrow(QuizWidgetApplicationException::quizWidgetNotFound);

        Widget widget = widgetRepository.findById(quizWidget.getWidgetId())
                .orElseThrow(QuizWidgetApplicationException::quizWidgetNotFound);

        if (!isTeacherOfLesson(user.getId(), widget.getLessonId()) && !isStudentOfLesson(user.getId(),
                widget.getLessonId())) {
            throw QuizWidgetApplicationException.forbidden();
        }

        List<Long> correctOptionIds = quizOptions.stream()
                .map(QuizOption::getId)
                .toList();

        return GetQuizAnswerResponse.from(correctOptionIds);
    }

    @Transactional
    public UpdateQuizOptionResponse updateQuizOption(User user, UpdateQuizOptionRequest request) {
        Long quizWidgetId = request.quizWidgetId();

        if (quizSubmissionRepository.existsByQuizWidgetId(quizWidgetId)) {
            throw QuizWidgetApplicationException.alreadyQuizSubmitted();
        }

        QuizWidget quizWidget = quizWidgetRepository.findById(quizWidgetId)
                .orElseThrow(QuizWidgetApplicationException::quizWidgetNotFound);

        Widget widget = widgetRepository.findById(quizWidget.getWidgetId())
                .orElseThrow(QuizWidgetApplicationException::quizWidgetNotFound);

        if (!isTeacherOfLesson(user.getId(), widget.getLessonId())) {
            throw QuizWidgetApplicationException.forbidden();
        }

        List<QuizOption> existingOptions = quizOptionRepository.findAllByQuizWidgetId(quizWidgetId);

        // 삭제 처리
        List<Long> requestOptionIds = request.options().stream()
                .map(QuizOptionRequest::quizOptionId)
                .filter(Objects::nonNull)
                .toList();

        List<QuizOption> toDelete = existingOptions.stream()
                .filter(option -> !requestOptionIds.contains(option.getId()))
                .toList();
        quizOptionRepository.deleteAllInBatch(toDelete);

        // 업데이트 또는 생성 처리
        for (QuizOptionRequest optionRequest : request.options()) {
            if (optionRequest.quizOptionId() != null) {
                QuizOption existingOption = existingOptions.stream()
                        .filter(option -> option.getId().equals(optionRequest.quizOptionId()))
                        .findFirst()
                        .orElseThrow(QuizWidgetApplicationException::quizOptionNotFound);

                existingOption.update(optionRequest.content(), optionRequest.isCorrect());
                quizOptionRepository.save(existingOption);
            } else {
                QuizOption newOption = new QuizOption(quizWidgetId, optionRequest.content(), optionRequest.isCorrect());
                quizOptionRepository.save(newOption);
            }
        }

        return UpdateQuizOptionResponse.from(quizWidgetId);
    }

    private boolean isTeacherOfLesson(Long userId, Long lessonId) {
        return teacherRepository.existsByLessonIdAndUserId(lessonId, userId);
    }

    private boolean isStudentOfLesson(Long userId, Long lessonId) {
        return studentRepository.existsByLessonIdAndUserId(lessonId, userId);
    }
}
