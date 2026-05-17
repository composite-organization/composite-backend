package kr.composite.api.lesson.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.List;

import kr.composite.api.lesson.ui.dto.request.CreateLessonRequest;
import kr.composite.api.lesson.ui.dto.request.FindMyLessonRequest;
import kr.composite.api.lesson.ui.dto.request.JoinLessonRequest;
import kr.composite.api.lesson.ui.dto.response.CreateLessonResponse;
import kr.composite.api.student.domain.Student;
import kr.composite.api.student.domain.StudentParticipateEvent;
import kr.composite.api.student.domain.StudentRepository;
import kr.composite.api.teacher.domain.Teacher;
import kr.composite.api.teacher.domain.TeacherRepository;
import kr.composite.api.user.domain.User;
import kr.composite.api.user.domain.UserName;
import kr.composite.api.user.domain.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class LessonServiceTest {

    @Autowired
    private LessonService lessonService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private TestEventListener testEventListener;

    private User savedUser;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public TestEventListener testEventListener() {
            return new TestEventListener();
        }
    }

    // 테스트용 이벤트 리스너
    static class TestEventListener {
        private final List<StudentParticipateEvent> events = new ArrayList<>();

        @EventListener
        public void listen(StudentParticipateEvent event) {
            events.add(event);
        }

        public List<StudentParticipateEvent> getEvents() {
            return events;
        }

        public void clear() {
            events.clear();
        }
    }

    @BeforeEach
    void setUp() {
        // 모든 테스트 전에 사용자 데이터 저장 및 이벤트 리스트 초기화
        savedUser = userRepository.save(new User(new UserName("테스트유저")));
        testEventListener.clear();
    }

    @Test
    void 학생이_수업에_정상적으로_참여하고_참여_이벤트가_발행된다() {
        // given
        final String lessonCode = "CODE123";
        CreateLessonResponse lessonResponse = lessonService.createLesson(savedUser, new CreateLessonRequest("수업자", "테스트수업", lessonCode, "pass123"));
        final Long lessonId = lessonResponse.lessonId();

        final String studentName = "참여자이름";
        final JoinLessonRequest request = new JoinLessonRequest(studentName);
        final User studentUser = userRepository.save(new User(new UserName("학생유저")));

        // when
        Long returnedLessonId = lessonService.joinStudent(lessonCode, studentUser, request);

        // then
        // 1. joinStudent가 lessonId를 반환하는지 확인
        assertThat(returnedLessonId).isEqualTo(lessonId);

        // 2. Student가 올바르게 저장되었는지 확인
        Student student = studentRepository.findByLessonIdAndUserId(lessonId, studentUser.getId()).orElseThrow();
        assertThat(student.getName().getValue()).isEqualTo(studentName);
        assertThat(student.getUserId()).isEqualTo(studentUser.getId());
        assertThat(student.getLessonId()).isEqualTo(lessonId);

        // 3. 이벤트가 발행되었는지 확인
        assertThat(testEventListener.getEvents()).hasSize(1);
        StudentParticipateEvent event = testEventListener.getEvents().get(0);
        assertThat(event.lessonId()).isEqualTo(lessonId);
        assertThat(event.student().getId()).isEqualTo(student.getId());
    }

    @Test
    void 이미_참여한_학생은_중복으로_참여할_수_없다() {
        // given
        final String lessonCode = "CODE123";
        lessonService.createLesson(savedUser, new CreateLessonRequest("수업자", "테스트수업", lessonCode, "pass123"));

        final User studentUser = userRepository.save(new User(new UserName("학생유저")));
        lessonService.joinStudent(lessonCode, studentUser, new JoinLessonRequest("첫번째참여"));

        // when & then
        final JoinLessonRequest requestForSecondAttempt = new JoinLessonRequest("두번째참여");
        assertThatThrownBy(() -> lessonService.joinStudent(lessonCode, studentUser, requestForSecondAttempt))
                .isInstanceOf(LessonApplicationException.class)
                .hasMessage("이미 참여한 사용자입니다.");
    }

    @Test
    void 수업_참여_요청_시_이름이_없으면_기존_사용자_이름으로_참여한다() {
        // given
        final String lessonCode = "CODE123";
        CreateLessonResponse lessonResponse = lessonService.createLesson(savedUser, new CreateLessonRequest("수업자", "테스트수업", lessonCode, "pass123"));
        final Long lessonId = lessonResponse.lessonId();

        final JoinLessonRequest request = new JoinLessonRequest(null); // 이름 없음
        final User studentUser = userRepository.save(new User(new UserName("학생유저")));

        // when
        lessonService.joinStudent(lessonCode, studentUser, request);

        // then
        Student student = studentRepository.findByLessonIdAndUserId(lessonId, studentUser.getId()).orElseThrow();
        assertThat(student.getName().getValue()).isEqualTo(studentUser.getName().getValue());
    }

    @Test
    void 수업을_정상적으로_생성하고_수업자이_등록된다() {
        // given
        final String teacherName = "수업자2";
        final String lessonName = "테스트수업2";
        final String newLessonCode = "NEWCODE";
        final String password = "password123";
        final CreateLessonRequest request = new CreateLessonRequest(teacherName, lessonName, newLessonCode, password);

        // when
        CreateLessonResponse response = lessonService.createLesson(savedUser, request);

        // then
        assertThat(response.lessonId()).isNotNull();
        assertThat(response.lessonName()).isEqualTo(lessonName);

        Teacher teacher = teacherRepository.findByLessonIdAndUserId(response.lessonId(), savedUser.getId()).orElseThrow();
        assertThat(teacher.getName().getValue()).isEqualTo(teacherName);
        assertThat(teacher.getUserId()).isEqualTo(savedUser.getId());
    }

    @Test
    void 생성된_수업을_코드와_비밀번호로_조회할_수_있다() {
        // given
        final String findLessonCode = "FIND123";
        final String password = "password123";
        CreateLessonResponse createdLesson = lessonService.createLesson(savedUser, new CreateLessonRequest("수업자", "조회수업", findLessonCode, password));
        final FindMyLessonRequest request = new FindMyLessonRequest(findLessonCode, password);

        // when
        AuthenticateLessonResult result = lessonService.readMyLesson(request);

        // then
        assertThat(result.lessonId()).isEqualTo(createdLesson.lessonId());
        assertThat(result.token()).isNotBlank();
    }

    @Test
    void 수업을_정상적으로_조회한다() {
        // given
        final String lessonCode = "CODE123";
        CreateLessonResponse createdLesson = lessonService.createLesson(savedUser, new CreateLessonRequest("수업자", "테스트수업", lessonCode, "pass123"));

        // when
        var lessonResponse = lessonService.readLesson(createdLesson.lessonId(), savedUser);

        // then
        assertThat(lessonResponse.lessonName()).isEqualTo("테스트수업");
    }

    @Test
    void 권한이_없는_수업_조회_시_예외가_발생한다() {
        // given
        final String lessonCode = "CODE123";
        CreateLessonResponse createdLesson = lessonService.createLesson(savedUser, new CreateLessonRequest("수업자", "테스트수업", lessonCode, "pass123"));

        User anotherUser = userRepository.save(new User(new UserName("다른유저")));

        // when & then
        assertThatThrownBy(() -> lessonService.readLesson(createdLesson.lessonId(), anotherUser))
                .isInstanceOf(LessonApplicationException.class)
                .hasMessage("수업 접근 권한이 없습니다.");
    }
}
