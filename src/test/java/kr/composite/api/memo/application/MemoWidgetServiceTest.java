package kr.composite.api.memo.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import jakarta.persistence.EntityManager;
import kr.composite.api.lesson.domain.Lesson;
import kr.composite.api.lesson.domain.LessonCode;
import kr.composite.api.lesson.domain.LessonName;
import kr.composite.api.lesson.domain.LessonPassword;
import kr.composite.api.memo.application.dto.request.MemoWidgetAddRequest;
import kr.composite.api.memo.application.dto.request.MemoWidgetFindRequest;
import kr.composite.api.memo.application.dto.request.MemoWidgetUpdateRequest;
import kr.composite.api.memo.application.dto.response.MemoWidgetResponse;
import kr.composite.api.memo.domain.MemoContent;
import kr.composite.api.memo.domain.MemoTitle;
import kr.composite.api.memo.domain.MemoWidget;
import kr.composite.api.memo.infrastructure.SpringDataJpaMemoWidgetRepository;
import kr.composite.api.student.domain.Student;
import kr.composite.api.student.domain.StudentName;
import kr.composite.api.teacher.domain.Teacher;
import kr.composite.api.teacher.domain.TeacherName;
import kr.composite.api.user.domain.User;
import kr.composite.api.user.domain.UserName;
import kr.composite.api.widget.domain.Widget;
import kr.composite.api.widget.domain.WidgetType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class MemoWidgetServiceTest {

    @Autowired
    EntityManager entityManager;

    @Autowired
    SpringDataJpaMemoWidgetRepository springDataJpaMemoWidgetRepository;

    @Autowired
    private MemoWidgetService memoWidgetService;

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
    void 선생님인_경우_메모를_조회할_수_있다() {
        // given
        makeUserTeacherOfLesson();
        Widget widget = new Widget(lesson.getId(), WidgetType.MEMO);
        entityManager.persist(widget);
        MemoTitle memoTitle = new MemoTitle("title");
        MemoContent memoContent = new MemoContent("content");
        MemoWidget memoWidget = new MemoWidget(widget.getId(), memoTitle, memoContent);
        entityManager.persist(memoWidget);

        entityManager.flush();
        entityManager.clear();

        // when
        MemoWidgetFindRequest memoWidgetFindRequest = MemoWidgetFindRequest.from(memoWidget.getId());
        MemoWidgetResponse memoWidgetResponse = memoWidgetService.getMemoWidget(user, memoWidgetFindRequest);

        // then
        assertThat(memoWidgetResponse.id()).isEqualTo(memoWidget.getId());
        assertThat(memoWidgetResponse.widgetId()).isEqualTo(widget.getId());
        assertThat(memoWidgetResponse.title()).isEqualTo("title");
        assertThat(memoWidgetResponse.content()).isEqualTo("content");
    }

    @Test
    void 학생인_경우_메모를_조회할_수_있다() {
        // given
        makeUserStudentOfLesson();
        Widget widget = new Widget(lesson.getId(), WidgetType.MEMO);
        entityManager.persist(widget);
        MemoTitle memoTitle = new MemoTitle("title");
        MemoContent memoContent = new MemoContent("content");
        MemoWidget memoWidget = new MemoWidget(widget.getId(), memoTitle, memoContent);
        entityManager.persist(memoWidget);

        entityManager.flush();
        entityManager.clear();

        // when
        MemoWidgetFindRequest memoWidgetFindRequest = MemoWidgetFindRequest.from(memoWidget.getId());
        MemoWidgetResponse memoWidgetResponse = memoWidgetService.getMemoWidget(user, memoWidgetFindRequest);

        // then
        assertThat(memoWidgetResponse.id()).isEqualTo(memoWidget.getId());
        assertThat(memoWidgetResponse.title()).isEqualTo("title");
    }

    @Test
    void 참여자가_아닌_경우_메모_조회시_예외가_발생한다() {
        // given
        Widget widget = new Widget(lesson.getId(), WidgetType.MEMO);
        entityManager.persist(widget);
        MemoTitle memoTitle = new MemoTitle("title");
        MemoContent memoContent = new MemoContent("content");
        MemoWidget memoWidget = new MemoWidget(widget.getId(), memoTitle, memoContent);
        entityManager.persist(memoWidget);

        entityManager.flush();
        entityManager.clear();

        MemoWidgetFindRequest memoWidgetFindRequest = MemoWidgetFindRequest.from(memoWidget.getId());

        // when & then
        assertThatThrownBy(() -> memoWidgetService.getMemoWidget(user, memoWidgetFindRequest)).isInstanceOf(
                MemoApplicationException.class);
    }

    @Test
    void 존재하지_않는_메모를_조회시_예외가_발생한다() {
        // given
        MemoWidgetFindRequest memoWidgetFindRequest = MemoWidgetFindRequest.from(9999L);
        // when & then
        assertThatThrownBy(() -> memoWidgetService.getMemoWidget(user, memoWidgetFindRequest)).isInstanceOf(
                MemoApplicationException.class);
    }

    @Test
    void 선생님인_경우_메모를_생성할_수_있다() {
        // given
        makeUserTeacherOfLesson();

        String title = "메모 제목";
        String content = "메모 내용";
        MemoWidgetAddRequest memoWidgetAddRequest = new MemoWidgetAddRequest(lesson.getId(), title, content);

        // when
        MemoWidgetResponse memoWidgetResponse = memoWidgetService.addMemoWidget(user, memoWidgetAddRequest);

        // then
        MemoWidget memoWidget = springDataJpaMemoWidgetRepository.findById(memoWidgetResponse.id()).orElse(null);

        assertThat(memoWidget).isNotNull();
        assertThat(memoWidget.getTitle().getValue()).isEqualTo(title);
        assertThat(memoWidget.getContent().getValue()).isEqualTo(content);
    }

    @Test
    void 선생님이_아닌_경우_메모_생성시_예외가_발생한다() {
        // given
        String title = "메모 제목";
        String content = "메모 내용";
        MemoWidgetAddRequest memoWidgetAddRequest = new MemoWidgetAddRequest(lesson.getId(), title, content);

        // when & then
        assertThatThrownBy(() -> memoWidgetService.addMemoWidget(user, memoWidgetAddRequest))
                .isInstanceOf(MemoApplicationException.class);
    }

    @Test
    void 선생님인_경우_메모를_수정할_수_있다() {
        // given
        makeUserTeacherOfLesson();

        String title = "메모 제목";
        String content = "메모 내용";
        Widget widget = new Widget(lesson.getId(), WidgetType.MEMO);
        entityManager.persist(widget);
        MemoTitle memoTitle = new MemoTitle(title);
        MemoContent memoContent = new MemoContent(content);
        MemoWidget memoWidget = new MemoWidget(widget.getId(), memoTitle, memoContent);
        entityManager.persist(memoWidget);

        entityManager.flush();
        entityManager.clear();

        String updateTitle = "메모 제목 업데이트";
        String updatedContent = "메모 내용 업데이트";
        MemoWidgetUpdateRequest memoWidgetUpdateRequest = new MemoWidgetUpdateRequest(memoWidget.getId(), updateTitle,
                updatedContent);

        // when
        MemoWidgetResponse memoWidgetResponse = memoWidgetService.updateMemoWidget(user, memoWidgetUpdateRequest);

        // then
        MemoWidget updatedMemoWidget = springDataJpaMemoWidgetRepository.findById(memoWidgetResponse.id()).orElse(null);

        assertThat(updatedMemoWidget).isNotNull();
        assertThat(updatedMemoWidget.getId()).isEqualTo(memoWidget.getId());
        assertThat(updatedMemoWidget.getTitle().getValue()).isEqualTo(updateTitle);
        assertThat(updatedMemoWidget.getContent().getValue()).isEqualTo(updatedContent);
    }

    @Test
    void 선생님이_아닌_경우_메모_수정시_예외가_발생한다() {
        // given
        Widget widget = new Widget(lesson.getId(), WidgetType.MEMO);
        entityManager.persist(widget);
        MemoTitle memoTitle = new MemoTitle("title");
        MemoContent memoContent = new MemoContent("content");
        MemoWidget memoWidget = new MemoWidget(widget.getId(), memoTitle, memoContent);
        entityManager.persist(memoWidget);

        entityManager.flush();
        entityManager.clear();

        MemoWidgetUpdateRequest memoWidgetUpdateRequest = new MemoWidgetUpdateRequest(memoWidget.getId(), "u title",
                "u content");

        // when & then
        assertThatThrownBy(() -> memoWidgetService.updateMemoWidget(user, memoWidgetUpdateRequest))
                .isInstanceOf(MemoApplicationException.class);
    }

    @Test
    void 선생님인_경우_메모를_삭제할_수_있다() {
        // given
        makeUserTeacherOfLesson();

        Widget widget = new Widget(lesson.getId(), WidgetType.MEMO);
        entityManager.persist(widget);
        String title = "메모 제목";
        String content = "메모 내용";
        MemoTitle memoTitle = new MemoTitle(title);
        MemoContent memoContent = new MemoContent(content);
        MemoWidget memoWidget = new MemoWidget(widget.getId(), memoTitle, memoContent);
        entityManager.persist(memoWidget);

        entityManager.flush();
        entityManager.clear();

        MemoWidgetFindRequest memoWidgetFindRequest = MemoWidgetFindRequest.from(memoWidget.getId());

        // when
        memoWidgetService.deleteMemoWidget(user, memoWidgetFindRequest);

        // then
        assertThat(springDataJpaMemoWidgetRepository.findById(memoWidget.getId())).isEmpty();
    }

    @Test
    void 선생님이_아닌_경우_메모_삭제시_예외가_발생한다() {
        // given
        Widget widget = new Widget(lesson.getId(), WidgetType.MEMO);
        entityManager.persist(widget);
        MemoTitle memoTitle = new MemoTitle("title");
        MemoContent memoContent = new MemoContent("content");
        MemoWidget memoWidget = new MemoWidget(widget.getId(), memoTitle, memoContent);
        entityManager.persist(memoWidget);

        entityManager.flush();
        entityManager.clear();

        MemoWidgetFindRequest memoWidgetFindRequest = MemoWidgetFindRequest.from(memoWidget.getId());

        // when & then
        assertThatThrownBy(() -> memoWidgetService.deleteMemoWidget(user, memoWidgetFindRequest))
                .isInstanceOf(MemoApplicationException.class);
    }
}
