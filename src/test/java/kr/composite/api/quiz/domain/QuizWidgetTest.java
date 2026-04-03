package kr.composite.api.quiz.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class QuizWidgetTest {

    @Test
    void 퀴즈_위젯을_생성할_수_있다() {
        // given
        Long widgetId = 1L;
        QuizTitle title = new QuizTitle("퀴즈 제목");
        QuizStatus status = QuizStatus.NOT_STARTED;

        // when
        QuizWidget quizWidget = new QuizWidget(widgetId, title, status);

        // then
        assertThat(quizWidget).isNotNull();
        assertThat(quizWidget.getWidgetId()).isEqualTo(widgetId);
        assertThat(quizWidget.getTitle()).isEqualTo(title);
        assertThat(quizWidget.getQuizStatus()).isEqualTo(status);
    }

    @Test
    void 퀴즈_위젯의_상태를_변경할_수_있다() {
        // given
        Long widgetId = 1L;
        QuizTitle title = new QuizTitle("퀴즈 제목");
        QuizStatus status = QuizStatus.NOT_STARTED;
        QuizWidget quizWidget = new QuizWidget(widgetId, title, status);

        QuizStatus newStatus = QuizStatus.IN_PROGRESS;

        // when
        quizWidget.updateStatus(newStatus);

        // then
        assertThat(quizWidget.getQuizStatus()).isEqualTo(newStatus);
    }
}
