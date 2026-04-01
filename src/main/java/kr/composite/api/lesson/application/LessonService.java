package kr.composite.api.lesson.application;

import java.util.Optional;
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
import kr.composite.api.participant.domain.Student;
import kr.composite.api.participant.domain.StudentName;
import kr.composite.api.participant.domain.StudentParticipateEvent;
import kr.composite.api.participant.domain.StudentRepository;
import kr.composite.api.participant.domain.Teacher;
import kr.composite.api.participant.domain.TeacherName;
import kr.composite.api.participant.domain.TeacherRepository;
import kr.composite.api.user.domain.User;
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
    private final CredentialCodec credentialCodec;

    @Transactional
    public void joinStudent(String lessonCodeValue, User user, JoinLessonRequest request) {
        LessonCode lessonCode = new LessonCode(lessonCodeValue);
        Lesson lesson = lessonRepository.findByLessonCode(lessonCode)
                .orElseThrow(() -> LessonApplicationException.cannotFindLesson());
        Long lessonId = lesson.getId();

        if (studentRepository.findByLessonIdAndUserId(lessonId, user.getId()).isPresent()) {
            throw LessonApplicationException.alreadyJoined();
        }
        StudentName studentName = new StudentName(
                Optional.ofNullable(request.name()).orElse(user.getName().getValue())
        );
        Student studentToSave = new Student(user.getId(), lessonId, studentName);
        Student savedStudent = studentRepository.save(studentToSave);

        eventPublisher.publishEvent(new StudentParticipateEvent(savedStudent, lessonId));
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
    public String findMyLesson(FindMyLessonRequest request) {
        LessonCode lessonCode = new LessonCode(request.lessonCode());
        Lesson lesson = lessonRepository.findByLessonCode(lessonCode)
                .orElseThrow(() -> LessonApplicationException.cannotFindLesson());

        LessonPassword lessonPassword = lesson.getPassword();
        lessonPassword.checkPassword(request.password());

        Teacher teacher = teacherRepository.findByLessonId(lesson.getId())
                .orElseThrow(() -> LessonApplicationException.cannotFindTeacher());

        CredentialPayload payload = new CredentialPayload(teacher.getLessonId());

        return credentialCodec.encode(payload);
    }

    @Transactional(readOnly = true)
    public GetLessonResponse readLesson(String lessonCodeValue, User user) {
        LessonCode lessonCode = new LessonCode(lessonCodeValue);
        Lesson lesson = lessonRepository.findByLessonCode(lessonCode)
                .orElseThrow(() -> LessonApplicationException.cannotFindLesson());
        Long lessonId = lesson.getId();

        Teacher teacher = teacherRepository.findByLessonId(lessonId)
                .orElseThrow(() -> LessonApplicationException.cannotFindTeacher());

        if (!teacher.getUserId().equals(user.getId())) {
            throw LessonApplicationException.noPermission();
        }

        return GetLessonResponse.from(lesson);
    }
}
