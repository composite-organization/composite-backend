package kr.composite.api.quiz.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

public class QuizTitleTest {

    @Test
    void 퀴즈_제목은_빈_값일_수_없다() {
        // given
        String title = null;

        // when & then
        assertThatThrownBy(() -> new QuizTitle(title))
                .isInstanceOf(QuizWidgetDomainException.class);
    }

    @Test
    void 퀴즈_제목의_길이가_유효하지_않으면_예외가_발생한다() {
        // given
        String tooShortTitle = "";
        String tooLongTitle = "a".repeat(201);

        // when & then
        assertThatThrownBy(() -> new QuizTitle(tooShortTitle))
                .isInstanceOf(QuizWidgetDomainException.class);
        assertThatThrownBy(() -> new QuizTitle(tooLongTitle))
                .isInstanceOf(QuizWidgetDomainException.class);
    }
}
