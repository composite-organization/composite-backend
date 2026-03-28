package kr.composite.api.lesson.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.List;

import kr.composite.api.lesson.application.LessonApplicationException;
import kr.composite.api.lesson.ui.dto.request.JoinLessonRequest;
import kr.composite.api.participant.domain.Participant;
import kr.composite.api.participant.domain.ParticipantRepository;
import kr.composite.api.participant.domain.Student;
import kr.composite.api.participant.domain.StudentParticipateEvent;
import kr.composite.api.participant.domain.StudentRepository;
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
    private ParticipantRepository participantRepository;
    @Autowired
    private StudentRepository studentRepository;
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
        final Long lessonId = 1L;
        final String participantName = "참여자이름";
        final JoinLessonRequest request = new JoinLessonRequest(participantName);

        // when
        lessonService.joinStudent(lessonId, savedUser, request);

        // then
        // 1. Participant가 올바르게 저장되었는지 확인
        Participant participant = participantRepository.findByLessonIdAndUserId(lessonId, savedUser.getId()).orElseThrow();
        assertThat(participant.getName().getValue()).isEqualTo(participantName);
        assertThat(participant.getUserId()).isEqualTo(savedUser.getId());
        assertThat(participant.getLessonId()).isEqualTo(lessonId);

        // 2. Student가 올바르게 저장되었는지 확인
        Student student = studentRepository.findByParticipantId(participant.getId()).orElseThrow();
        assertThat(student.getParticipantId()).isEqualTo(participant.getId());

        // 3. 이벤트가 발행되었는지 확인
        assertThat(testEventListener.getEvents()).hasSize(1);
        StudentParticipateEvent event = testEventListener.getEvents().get(0);
        assertThat(event.lessonId()).isEqualTo(lessonId);
        assertThat(event.student().getId()).isEqualTo(student.getId());
    }

    @Test
    void 이미_참여한_학생은_중복으로_참여할_수_없다() {
        // given
        final Long lessonId = 1L;
        lessonService.joinStudent(lessonId, savedUser, new JoinLessonRequest("첫번째참여"));

        // when & then
        final JoinLessonRequest requestForSecondAttempt = new JoinLessonRequest("두번째참여");
        assertThatThrownBy(() -> lessonService.joinStudent(lessonId, savedUser, requestForSecondAttempt))
                .isInstanceOf(LessonApplicationException.class)
                .hasMessage("이미 참여한 사용자입니다.");
    }

    @Test
    void 수업_참여_요청_시_이름이_없으면_기존_사용자_이름으로_참여한다() {
        // given
        final Long lessonId = 1L;
        final JoinLessonRequest request = new JoinLessonRequest(null); // 이름 없음

        // when
        lessonService.joinStudent(lessonId, savedUser, request);

        // then
        Participant participant = participantRepository.findByLessonIdAndUserId(lessonId, savedUser.getId()).orElseThrow();
        assertThat(participant.getName().getValue()).isEqualTo(savedUser.getName().getValue());
    }
}
