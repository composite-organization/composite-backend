package kr.composite.api.attachment.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import jakarta.persistence.EntityManager;
import kr.composite.api.attachment.application.dto.request.AttachmentWidgetAddRequest;
import kr.composite.api.attachment.application.dto.request.AttachmentWidgetDeleteRequest;
import kr.composite.api.attachment.application.dto.request.AttachmentWidgetFindRequest;
import kr.composite.api.attachment.application.dto.response.AttachmentWidgetResponse;
import kr.composite.api.attachment.domain.AttachmentWidget;
import kr.composite.api.attachment.domain.AttachmentWidgetRepository;
import kr.composite.api.lesson.domain.Lesson;
import kr.composite.api.lesson.domain.LessonCode;
import kr.composite.api.lesson.domain.LessonName;
import kr.composite.api.lesson.domain.LessonPassword;
import kr.composite.api.student.domain.Student;
import kr.composite.api.student.domain.StudentName;
import kr.composite.api.teacher.domain.Teacher;
import kr.composite.api.teacher.domain.TeacherName;
import kr.composite.api.user.domain.User;
import kr.composite.api.user.domain.UserName;
import kr.composite.api.widget.domain.Widget;
import kr.composite.api.widget.domain.WidgetRepository;
import kr.composite.api.widget.domain.WidgetType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class AttachmentWidgetServiceTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private AttachmentWidgetRepository attachmentWidgetRepository;

    @Autowired
    private WidgetRepository widgetRepository;

    @Autowired
    private AttachmentWidgetService attachmentWidgetService;

    private User user;
    private Lesson lesson;

    @BeforeEach
    void setUp() {
        user = new User(new UserName("testUser"));
        entityManager.persist(user);

        lesson = new Lesson(new LessonName("lesson"), new LessonCode("code"), new LessonPassword("pass123"));
        entityManager.persist(lesson);
    }

    private void makeUserTeacherOfLesson() {
        Teacher teacher = new Teacher(user.getId(), lesson.getId(), new TeacherName("teacher"));
        entityManager.persist(teacher);
    }

    private void makeUserStudentOfLesson() {
        Student student = new Student(user.getId(), lesson.getId(), new StudentName("student"));
        entityManager.persist(student);
    }

    @Test
    @DisplayName("선생님인 경우 자료 공유 위젯을 조회할 수 있다")
    void 선생님인_경우_자료_공유_위젯을_조회할_수_있다() {
        // given
        makeUserTeacherOfLesson();
        Widget widget = new Widget(lesson.getId(), WidgetType.ATTACHMENT);
        widgetRepository.save(widget);

        AttachmentWidget attachmentWidget = new AttachmentWidget(widget.getId());
        attachmentWidgetRepository.save(attachmentWidget);

        entityManager.flush();
        entityManager.clear();

        AttachmentWidgetFindRequest request = AttachmentWidgetFindRequest.from(attachmentWidget.getId());

        // when
        AttachmentWidgetResponse response = attachmentWidgetService.getAttachmentWidget(user, request);

        // then
        assertThat(response.id()).isEqualTo(attachmentWidget.getId());
        assertThat(response.widgetId()).isEqualTo(widget.getId());
    }

    @Test
    @DisplayName("학생인 경우 자료 공유 위젯을 조회할 수 있다")
    void 학생인_경우_자료_공유_위젯을_조회할_수_있다() {
        // given
        makeUserStudentOfLesson();
        Widget widget = new Widget(lesson.getId(), WidgetType.ATTACHMENT);
        widgetRepository.save(widget);

        AttachmentWidget attachmentWidget = new AttachmentWidget(widget.getId());
        attachmentWidgetRepository.save(attachmentWidget);

        entityManager.flush();
        entityManager.clear();

        AttachmentWidgetFindRequest request = AttachmentWidgetFindRequest.from(attachmentWidget.getId());

        // when
        AttachmentWidgetResponse response = attachmentWidgetService.getAttachmentWidget(user, request);

        // then
        assertThat(response.id()).isEqualTo(attachmentWidget.getId());
    }

    @Test
    @DisplayName("참여자가 아닌 경우 자료 공유 위젯 조회 시 예외가 발생한다")
    void 참여자가_아닌_경우_자료_공유_위젯_조회_예외_테스트() {
        // given
        Widget widget = new Widget(lesson.getId(), WidgetType.ATTACHMENT);
        widgetRepository.save(widget);

        AttachmentWidget attachmentWidget = new AttachmentWidget(widget.getId());
        attachmentWidgetRepository.save(attachmentWidget);

        entityManager.flush();
        entityManager.clear();

        AttachmentWidgetFindRequest request = AttachmentWidgetFindRequest.from(attachmentWidget.getId());

        // when & then
        assertThatThrownBy(() -> attachmentWidgetService.getAttachmentWidget(user, request))
                .isInstanceOf(AttachmentApplicationException.class);
    }

    @Test
    @DisplayName("선생님인 경우 자료 공유 위젯을 생성할 수 있다")
    void 선생님인_경우_자료_공유_위젯을_생성할_수_있다() {
        // given
        makeUserTeacherOfLesson();
        AttachmentWidgetAddRequest request = new AttachmentWidgetAddRequest(lesson.getId());

        // when
        AttachmentWidgetResponse response = attachmentWidgetService.addAttachmentWidget(user, request);

        // then
        AttachmentWidget savedWidget = attachmentWidgetRepository.findById(response.id()).orElse(null);

        assertThat(savedWidget).isNotNull();
        assertThat(savedWidget.getWidgetId()).isNotNull();

        Widget widget = widgetRepository.findById(savedWidget.getWidgetId()).orElse(null);
        assertThat(widget).isNotNull();
        assertThat(widget.getWidgetType()).isEqualTo(WidgetType.ATTACHMENT);
    }

    @Test
    @DisplayName("선생님이 아닌 경우 자료 공유 위젯 생성 시 예외가 발생한다")
    void 선생님이_아닌_경우_자료_공유_위젯_생성_예외_테스트() {
        // given
        AttachmentWidgetAddRequest request = new AttachmentWidgetAddRequest(lesson.getId());

        // when & then
        assertThatThrownBy(() -> attachmentWidgetService.addAttachmentWidget(user, request))
                .isInstanceOf(AttachmentApplicationException.class);
    }

    @Test
    @DisplayName("선생님인 경우 자료 공유 위젯을 삭제할 수 있다")
    void 선생님인_경우_자료_공유_위젯을_삭제할_수_있다() {
        // given
        makeUserTeacherOfLesson();
        Widget widget = new Widget(lesson.getId(), WidgetType.ATTACHMENT);
        widgetRepository.save(widget);

        AttachmentWidget attachmentWidget = new AttachmentWidget(widget.getId());
        attachmentWidgetRepository.save(attachmentWidget);

        entityManager.flush();
        entityManager.clear();

        AttachmentWidgetDeleteRequest request = AttachmentWidgetDeleteRequest.from(attachmentWidget.getId());

        // when
        attachmentWidgetService.deleteAttachmentWidget(user, request);

        // then
        assertThat(attachmentWidgetRepository.findById(attachmentWidget.getId())).isEmpty();
        assertThat(widgetRepository.findById(widget.getId())).isEmpty();
    }

    @Test
    @DisplayName("선생님이 아닌 경우 자료 공유 위젯 삭제 시 예외가 발생한다")
    void 선생님이_아닌_경우_자료_공유_위젯_삭제_예외_테스트() {
        // given
        Widget widget = new Widget(lesson.getId(), WidgetType.ATTACHMENT);
        widgetRepository.save(widget);

        AttachmentWidget attachmentWidget = new AttachmentWidget(widget.getId());
        attachmentWidgetRepository.save(attachmentWidget);

        entityManager.flush();
        entityManager.clear();

        AttachmentWidgetDeleteRequest request = AttachmentWidgetDeleteRequest.from(attachmentWidget.getId());

        // when & then
        assertThatThrownBy(() -> attachmentWidgetService.deleteAttachmentWidget(user, request))
                .isInstanceOf(AttachmentApplicationException.class);
    }
}
