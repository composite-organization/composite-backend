package kr.composite.api.lesson.application;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import kr.composite.api.authentication.domain.CredentialCodec;
import kr.composite.api.authentication.domain.CredentialPayload;
import kr.composite.api.lesson.domain.Lesson;
import kr.composite.api.lesson.domain.LessonCode;
import kr.composite.api.lesson.domain.LessonName;
import kr.composite.api.lesson.domain.LessonPassword;
import kr.composite.api.lesson.domain.LessonRepository;
import kr.composite.api.lesson.ui.dto.request.CreateLessonRequest;
import kr.composite.api.lesson.ui.dto.request.FindMyLessonRequest;
import kr.composite.api.lesson.ui.dto.request.JoinLessonRequest;
import kr.composite.api.lesson.ui.dto.response.CreateLessonResponse;
import kr.composite.api.lesson.ui.dto.response.GetLessonResponse;
import kr.composite.api.lesson.ui.dto.response.GetWidgetIdsResponse;
import kr.composite.api.student.domain.Student;
import kr.composite.api.student.domain.StudentName;
import kr.composite.api.student.domain.StudentParticipateEvent;
import kr.composite.api.student.domain.StudentRepository;
import kr.composite.api.teacher.domain.Teacher;
import kr.composite.api.teacher.domain.TeacherName;
import kr.composite.api.teacher.domain.TeacherRepository;
import kr.composite.api.user.domain.User;
import kr.composite.api.attachment.domain.AttachmentWidget;
import kr.composite.api.attachment.domain.AttachmentWidgetRepository;
import kr.composite.api.memo.domain.MemoWidget;
import kr.composite.api.memo.domain.MemoWidgetRepository;
import kr.composite.api.quiz.domain.QuizWidget;
import kr.composite.api.quiz.domain.QuizWidgetRepository;
import kr.composite.api.vote.domain.VoteWidget;
import kr.composite.api.vote.domain.VoteWidgetRepository;
import kr.composite.api.widget.domain.Widget;
import kr.composite.api.widget.domain.WidgetRepository;
import kr.composite.api.widget.domain.WidgetType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LessonService {

    private final StudentRepository studentRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final LessonRepository lessonRepository;
    private final TeacherRepository teacherRepository;
    private final WidgetRepository widgetRepository;
    private final MemoWidgetRepository memoWidgetRepository;
    private final AttachmentWidgetRepository attachmentWidgetRepository;
    private final QuizWidgetRepository quizWidgetRepository;
    private final VoteWidgetRepository voteWidgetRepository;
    private final CredentialCodec credentialCodec;

    @Transactional
    public Long joinStudent(String lessonCodeValue, User user, JoinLessonRequest request) {
        LessonCode lessonCode = new LessonCode(lessonCodeValue);
        Lesson lesson = lessonRepository.findByLessonCode(lessonCode)
                .orElseThrow(() -> LessonApplicationException.cannotFindLesson());
        Long lessonId = lesson.getId();

        if (studentRepository.existsByLessonIdAndUserId(lessonId, user.getId())) {
            throw LessonApplicationException.alreadyJoined();
        }

        StudentName studentName = new StudentName(
                Optional.ofNullable(request.name()).orElse(user.getName().getValue())
        );
        Student studentToSave = new Student(user.getId(), lessonId, studentName);
        Student savedStudent = studentRepository.save(studentToSave);

        eventPublisher.publishEvent(new StudentParticipateEvent(savedStudent, lessonId));

        return lessonId;
    }

    @Transactional
    public CreateLessonResponse createLesson(User user, CreateLessonRequest request) {
        LessonName lessonName = new LessonName(request.lessonName());
        LessonCode lessonCode = new LessonCode(request.lessonCode());
        LessonPassword lessonPassword = new LessonPassword(request.password());
        Lesson lesson = new Lesson(lessonName, lessonCode, lessonPassword);
        lessonRepository.save(lesson);

        TeacherName teacherName = new TeacherName(request.teacherName());
        Teacher teacher = new Teacher(user.getId(), lesson.getId(), teacherName);
        teacherRepository.save(teacher);

        CreateLessonResponse createLessonResponse = new CreateLessonResponse(
                lesson.getId(),
                lesson.getName().getValue()
        );

        return createLessonResponse;
    }

    @Transactional(readOnly = true)
    public AuthenticateLessonResult readMyLesson(FindMyLessonRequest request) {
        LessonCode lessonCode = new LessonCode(request.lessonCode());
        Lesson lesson = lessonRepository.findByLessonCode(lessonCode)
                .orElseThrow(() -> LessonApplicationException.cannotFindLesson());

        LessonPassword lessonPassword = lesson.getPassword();
        lessonPassword.verify(request.password());

        Teacher teacher = teacherRepository.findByLessonId(lesson.getId())
                .orElseThrow(() -> LessonApplicationException.cannotFindTeacher());

        CredentialPayload payload = new CredentialPayload(teacher.getUserId());
        String token = credentialCodec.encode(payload);

        return new AuthenticateLessonResult(token, lesson.getId());
    }

    @Transactional(readOnly = true)
    public GetLessonResponse readLesson(Long lessonId, User user) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> LessonApplicationException.cannotFindLesson());

        Teacher teacher = teacherRepository.findByLessonId(lessonId)
                .orElseThrow(() -> LessonApplicationException.cannotFindTeacher());

        validateParticipant(lessonId, user.getId());

        return GetLessonResponse.from(lesson.getName().getValue(), teacher.getName().getValue());
    }

    @Transactional(readOnly = true)
    public GetWidgetIdsResponse readWidgetIds(Long lessonId, User user) {
        lessonRepository.findById(lessonId)
                .orElseThrow(LessonApplicationException::cannotFindLesson);

        validateParticipant(lessonId, user.getId());

        List<Widget> widgets = widgetRepository.findAllByLessonId(lessonId);
        Map<WidgetType, List<Long>> widgetIdsByType = groupWidgetIdsByType(widgets);

        Map<String, List<Long>> specificWidgetIds = fetchSpecificWidgetIds(widgetIdsByType);

        return GetWidgetIdsResponse.from(specificWidgetIds);
    }

    private Map<WidgetType, List<Long>> groupWidgetIdsByType(List<Widget> widgets) {
        return widgets.stream()
                .collect(Collectors.groupingBy(
                        Widget::getWidgetType,
                        Collectors.mapping(Widget::getId, Collectors.toList())
                ));
    }

    private Map<String, List<Long>> fetchSpecificWidgetIds(Map<WidgetType, List<Long>> widgetIdsByType) {
        Map<String, List<Long>> result = new HashMap<>();

        widgetIdsByType.forEach((type, ids) -> {
            List<Long> specificIds = switch (type) {
                case MEMO -> memoWidgetRepository.findAllByWidgetIdIn(ids).stream().map(MemoWidget::getId).toList();
                case ATTACHMENT -> attachmentWidgetRepository.findAllByWidgetIdIn(ids).stream().map(AttachmentWidget::getId).toList();
                case QUIZ -> quizWidgetRepository.findAllByWidgetIdIn(ids).stream().map(QuizWidget::getId).toList();
                case VOTE -> voteWidgetRepository.findAllByWidgetIdIn(ids).stream().map(VoteWidget::getId).toList();
            };
            result.put(type.name().toLowerCase(), specificIds);
        });

        return result;
    }

    private void validateParticipant(Long lessonId, Long userId) {
        boolean isTeacher = teacherRepository.existsByLessonIdAndUserId(lessonId, userId);
        boolean isStudent = studentRepository.existsByLessonIdAndUserId(lessonId, userId);

        if (!isTeacher && !isStudent) {
            throw LessonApplicationException.noPermission();
        }
    }
}
