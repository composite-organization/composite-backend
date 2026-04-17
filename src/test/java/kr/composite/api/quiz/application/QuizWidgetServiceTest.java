package kr.composite.api.quiz.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import jakarta.persistence.EntityManager;
import java.util.List;
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
import kr.composite.api.student.domain.Student;
import kr.composite.api.student.domain.StudentName;
import kr.composite.api.teacher.domain.Teacher;
import kr.composite.api.teacher.domain.TeacherName;
import kr.composite.api.user.domain.User;
import kr.composite.api.user.domain.UserName;
import kr.composite.api.widget.domain.Widget;
import kr.composite.api.widget.domain.WidgetType;
import kr.composite.api.widget.domain.WidgetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class QuizWidgetServiceTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private QuizWidgetService quizWidgetService;

    @Autowired
    private WidgetRepository widgetRepository;

    @Autowired
    private QuizWidgetRepository quizWidgetRepository;

    @Autowired
    private QuizOptionRepository quizOptionRepository;

    @Autowired
    private QuizSubmissionRepository quizSubmissionRepository;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User(new UserName("사용자"));
        entityManager.persist(user);

        Teacher teacher = new Teacher(user.getId(), 1L, new TeacherName("선생님"));
        entityManager.persist(teacher);

        entityManager.flush();
    }

    @Test
    void 퀴즈_위젯을_추가할_수_있다() {
        // given
        CreateQuizWidgetRequest request = new CreateQuizWidgetRequest(
                1L,
                "새 퀴즈",
                List.of(new CreateQuizWidgetRequest.QuizOptionRequest("옵션1", true))
        );

        // when
        CreateQuizWidgetResponse response = quizWidgetService.addQuizWidget(user, request);

        // then
        assertThat(response.quizWidgetId()).isNotNull();
        QuizWidget quizWidget = quizWidgetRepository.findById(response.quizWidgetId()).orElseThrow();
        assertThat(quizWidget.getTitle().getValue()).isEqualTo("새 퀴즈");

        List<QuizOption> options = quizOptionRepository.findAllByQuizWidgetId(quizWidget.getId());
        assertThat(options).hasSize(1);
        assertThat(options.get(0).getContent()).isEqualTo("옵션1");
    }

    @Test
    void 퀴즈_위젯을_조회할_수_있다() {
        // given
        Widget widget = new Widget(1L, WidgetType.QUIZ);
        widgetRepository.save(widget);
        QuizWidget quizWidget = new QuizWidget(widget.getId(), new QuizTitle("조회 퀴즈"), QuizStatus.IN_PROGRESS);
        quizWidgetRepository.save(quizWidget);

        QuizOption correctOption = new QuizOption(quizWidget.getId(), "정답", true);
        quizOptionRepository.save(correctOption);

        Student student = new Student(user.getId(), 1L, new StudentName("학생"));
        entityManager.persist(student);
        quizSubmissionRepository.save(new QuizSubmission(student.getId(), quizWidget.getId(), correctOption.getId()));

        // when
        GetQuizWidgetResponse response = quizWidgetService.readQuizWidget(user, quizWidget.getId());

        // then
        assertThat(response.title()).isEqualTo("조회 퀴즈");
        assertThat(response.correctRate()).isEqualTo(100);
        assertThat(response.submittedOptionIds()).containsExactly(correctOption.getId());
        assertThat(response.participationResponse().totalParticipantCount()).isEqualTo(1);
        assertThat(response.options()).hasSize(1);
        assertThat(response.options().get(0).content()).isEqualTo("정답");
    }

    @Test
    void 퀴즈_조회_시_참여_현황을_함께_반환한다() {
        // given
        Widget widget = new Widget(1L, WidgetType.QUIZ);
        widgetRepository.save(widget);
        QuizWidget quizWidget = new QuizWidget(widget.getId(), new QuizTitle("참여 현황 퀴즈"), QuizStatus.IN_PROGRESS);
        quizWidgetRepository.save(quizWidget);

        QuizOption option1 = new QuizOption(quizWidget.getId(), "옵션1", true);
        quizOptionRepository.save(option1);

        Student student1 = new Student(user.getId(), 1L, new StudentName("학생1"));
        entityManager.persist(student1);

        User user2 = new User(new UserName("사용자2"));
        entityManager.persist(user2);
        Student student2 = new Student(user2.getId(), 1L, new StudentName("학생2"));
        entityManager.persist(student2);

        quizSubmissionRepository.save(new QuizSubmission(student1.getId(), quizWidget.getId(), option1.getId()));
        quizSubmissionRepository.save(new QuizSubmission(student2.getId(), quizWidget.getId(), option1.getId()));

        entityManager.flush();
        entityManager.clear();

        // when
        GetQuizWidgetResponse response = quizWidgetService.readQuizWidget(user, quizWidget.getId());

        // then
        assertThat(response.participationResponse().totalParticipantCount()).isEqualTo(2);
        assertThat(response.participationResponse().optionStatuses().get(0).participantNames())
                .containsExactlyInAnyOrder("학생1", "학생2");
    }

    @Test
    void 학생은_자신이_제출한_답변을_함께_조회한다() {
        // given
        Widget widget = new Widget(1L, WidgetType.QUIZ);
        widgetRepository.save(widget);
        QuizWidget quizWidget = new QuizWidget(widget.getId(), new QuizTitle("조회 퀴즈"), QuizStatus.IN_PROGRESS);
        quizWidgetRepository.save(quizWidget);

        QuizOption option1 = new QuizOption(quizWidget.getId(), "옵션1", true);
        QuizOption option2 = new QuizOption(quizWidget.getId(), "옵션2", false);
        quizOptionRepository.saveAll(List.of(option1, option2));

        Student student = new Student(user.getId(), 1L, new StudentName("학생"));
        entityManager.persist(student);
        quizSubmissionRepository.save(new QuizSubmission(student.getId(), quizWidget.getId(), option1.getId()));

        entityManager.flush();
        entityManager.clear();

        // when
        GetQuizWidgetResponse response = quizWidgetService.readQuizWidget(user, quizWidget.getId());

        // then
        assertThat(response.submittedOptionIds()).containsExactly(option1.getId());
    }

    @Test
    void 제출이_없는_퀴즈_조회_시_정답률은_0이다() {
        // given
        Widget widget = new Widget(1L, WidgetType.QUIZ);
        widgetRepository.save(widget);
        QuizWidget quizWidget = new QuizWidget(widget.getId(), new QuizTitle("제출 없는 퀴즈"), QuizStatus.IN_PROGRESS);
        quizWidgetRepository.save(quizWidget);

        // when
        GetQuizWidgetResponse response = quizWidgetService.readQuizWidget(user, quizWidget.getId());

        // then
        assertThat(response.correctRate()).isEqualTo(0);
    }

    @Test
    void 존재하지_않는_퀴즈_위젯_조회_시_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> quizWidgetService.readQuizWidget(user, 999L))
                .isInstanceOf(QuizWidgetApplicationException.class);
    }

    @Test
    void 퀴즈_위젯_상태를_변경할_수_있다() {
        // given
        Widget widget = new Widget(1L, WidgetType.QUIZ);
        widgetRepository.save(widget);
        QuizWidget quizWidget = new QuizWidget(widget.getId(), new QuizTitle("상태 변경 퀴즈"), QuizStatus.NOT_STARTED);
        quizWidgetRepository.save(quizWidget);

        UpdateQuizWidgetStatusRequest request = new UpdateQuizWidgetStatusRequest("진행 중");

        // when
        quizWidgetService.updateQuizWidgetStatus(user, quizWidget.getId(), request);

        // then
        QuizWidget updated = quizWidgetRepository.findById(quizWidget.getId()).orElseThrow();
        assertThat(updated.getQuizStatus()).isEqualTo(QuizStatus.IN_PROGRESS);
    }

    @Test
    void 퀴즈_위젯을_삭제할_수_있다() {
        // given
        Widget widget = new Widget(1L, WidgetType.QUIZ);
        widgetRepository.save(widget);
        QuizWidget quizWidget = new QuizWidget(widget.getId(), new QuizTitle("삭제 퀴즈"), QuizStatus.NOT_STARTED);
        quizWidgetRepository.save(quizWidget);
        quizOptionRepository.save(new QuizOption(quizWidget.getId(), "옵션", true));

        // when
        quizWidgetService.deleteQuizWidget(user, quizWidget.getId());

        // then
        assertThat(quizWidgetRepository.findById(quizWidget.getId())).isEmpty();
        assertThat(quizOptionRepository.findAllByQuizWidgetId(quizWidget.getId())).isEmpty();
    }

    @Test
    void 퀴즈를_제출할_수_있다() {
        // given
        Widget widget = new Widget(1L, WidgetType.QUIZ);
        widgetRepository.save(widget);

        QuizWidget quizWidget = new QuizWidget(widget.getId(), new QuizTitle("제출 퀴즈"), QuizStatus.IN_PROGRESS);
        quizWidgetRepository.save(quizWidget);

        QuizOption option1 = new QuizOption(quizWidget.getId(), "정답 1", true);
        QuizOption option2 = new QuizOption(quizWidget.getId(), "정답 2", true);
        quizOptionRepository.saveAll(List.of(option1, option2));

        Student student = new Student(user.getId(), 1L, new StudentName("학생"));
        entityManager.persist(student);
        entityManager.flush();

        SubmitQuizSubmissionRequest request = new SubmitQuizSubmissionRequest(
                List.of(option1.getId(), option2.getId())
        );

        // when
        quizWidgetService.submitQuizSubmission(user, quizWidget.getId(), request);

        // then
        assertThat(quizSubmissionRepository.countByQuizWidgetId(quizWidget.getId())).isEqualTo(2);
    }

    @Test
    void 진행_중이_아닌_퀴즈는_제출할_수_없다() {
        // given
        Widget widget = new Widget(1L, WidgetType.QUIZ);
        widgetRepository.save(widget);
        QuizWidget quizWidget = new QuizWidget(widget.getId(), new QuizTitle("제출 불가 퀴즈"), QuizStatus.NOT_STARTED);
        quizWidgetRepository.save(quizWidget);
        QuizOption option = new QuizOption(quizWidget.getId(), "옵션", true);
        quizOptionRepository.save(option);

        Student student = new Student(user.getId(), 1L, new StudentName("학생"));
        entityManager.persist(student);
        entityManager.flush();

        SubmitQuizSubmissionRequest request = new SubmitQuizSubmissionRequest(List.of(option.getId()));

        // when & then
        assertThatThrownBy(() -> quizWidgetService.submitQuizSubmission(user, quizWidget.getId(), request))
                .isInstanceOf(QuizWidgetApplicationException.class);
    }

    @Test
    void 퀴즈_정답을_조회할_수_있다() {
        // given
        Widget widget = new Widget(1L, WidgetType.QUIZ);
        widgetRepository.save(widget);
        QuizWidget quizWidget = new QuizWidget(widget.getId(), new QuizTitle("정답 조회 퀴즈"), QuizStatus.ENDED);
        quizWidgetRepository.save(quizWidget);
        QuizOption correctOption = new QuizOption(quizWidget.getId(), "정답", true);
        quizOptionRepository.save(correctOption);

        // when
        GetQuizAnswerResponse response = quizWidgetService.readQuizAnswer(user, quizWidget.getId());

        // then
        assertThat(response.answerQuizOptionIds()).containsExactly(correctOption.getId());
    }

    @Test
    void 퀴즈_옵션을_업데이트_할_수_있다() {
        // given
        Widget widget = new Widget(1L, WidgetType.QUIZ);
        widgetRepository.save(widget);

        QuizWidget quizWidget = new QuizWidget(widget.getId(), new QuizTitle("퀴즈 제목"), QuizStatus.NOT_STARTED);
        quizWidgetRepository.save(quizWidget);

        QuizOption existingOption = new QuizOption(quizWidget.getId(), "기존 옵션", false);
        QuizOption deleteOption = new QuizOption(quizWidget.getId(), "삭제될 옵션", false);
        quizOptionRepository.saveAll(List.of(existingOption, deleteOption));

        entityManager.flush();
        entityManager.clear();

        UpdateQuizOptionRequest request = new UpdateQuizOptionRequest(
                quizWidget.getId(),
                List.of(
                        new QuizOptionRequest(existingOption.getId(), "수정된 옵션", true),
                        new QuizOptionRequest(null, "새로운 옵션", false)
                )
        );

        // when
        quizWidgetService.updateQuizOption(user, request);

        // then
        List<QuizOption> options = quizOptionRepository.findAllByQuizWidgetId(quizWidget.getId());
        assertThat(options).hasSize(2);

        QuizOption updated = options.stream()
                .filter(o -> o.getId().equals(existingOption.getId()))
                .findFirst()
                .orElseThrow();
        assertThat(updated.getContent()).isEqualTo("수정된 옵션");
        assertThat(updated.isCorrect()).isTrue();

        QuizOption added = options.stream()
                .filter(o -> !o.getId().equals(existingOption.getId()))
                .findFirst()
                .orElseThrow();
        assertThat(added.getContent()).isEqualTo("새로운 옵션");
        assertThat(added.isCorrect()).isFalse();

        assertThat(options.stream().anyMatch(o -> o.getId().equals(deleteOption.getId()))).isFalse();
    }

    @Test
    void 퀴즈에_제출물이_있는_경우_옵션을_수정할_수_없다() {
        // given
        Widget widget = new Widget(1L, WidgetType.QUIZ);
        widgetRepository.save(widget);

        QuizWidget quizWidget = new QuizWidget(widget.getId(), new QuizTitle("퀴즈 제목"), QuizStatus.NOT_STARTED);
        quizWidgetRepository.save(quizWidget);

        QuizOption option = new QuizOption(quizWidget.getId(), "옵션", true);
        quizOptionRepository.save(option);

        Student student = new Student(user.getId(), 1L, new StudentName("학생"));
        entityManager.persist(student);
        QuizSubmission submission = new QuizSubmission(student.getId(), quizWidget.getId(), option.getId());
        quizSubmissionRepository.save(submission);

        entityManager.flush();
        entityManager.clear();

        UpdateQuizOptionRequest request = new UpdateQuizOptionRequest(
                quizWidget.getId(),
                List.of(new QuizOptionRequest(option.getId(), "수정 시도", true))
        );

        // when & then
        assertThatThrownBy(() -> quizWidgetService.updateQuizOption(user, request))
                .isInstanceOf(QuizWidgetApplicationException.class);
    }
}
