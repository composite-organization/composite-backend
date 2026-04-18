package kr.composite.api.quiz.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class QuizOptionTest {

    @Test
    void 퀴즈_옵션을_생성할_수_있다() {
        // given
        Long quizWidgetId = 1L;
        String content = "옵션 내용";
        boolean isCorrect = true;

        // when
        QuizOption quizOption = new QuizOption(quizWidgetId, content, isCorrect);

        // then
        assertThat(quizOption).isNotNull();
        assertThat(quizOption.getQuizWidgetId()).isEqualTo(quizWidgetId);
        assertThat(quizOption.getContent()).isEqualTo(content);
        assertThat(quizOption.isCorrect()).isEqualTo(isCorrect);
    }

    @Test
    void 퀴즈_옵션을_수정할_수_있다() {
        // given
        Long quizWidgetId = 1L;
        String content = "옵션 내용";
        boolean isCorrect = false;
        QuizOption quizOption = new QuizOption(quizWidgetId, content, isCorrect);

        String updateContent = "수정된 옵션 내용";
        boolean updateIsCorrect = true;

        // when
        quizOption.update(updateContent, updateIsCorrect);

        // then
        assertThat(quizOption.getContent()).isEqualTo(updateContent);
        assertThat(quizOption.isCorrect()).isEqualTo(updateIsCorrect);
    }
}
