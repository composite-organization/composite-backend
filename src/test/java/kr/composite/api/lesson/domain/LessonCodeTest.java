package kr.composite.api.lesson.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class LessonCodeTest {

    @Test
    void 유효한_코드로_수업_코드를_생성할_수_있다() {
        // given
        String code = "ABCD12";

        // when
        LessonCode lessonCode = new LessonCode(code);

        // then
        assertThat(lessonCode).isNotNull();
    }

    @Nested
    @DisplayName("수업 코드 검증")
    class Validation {

        @Test
        void 코드가_null이면_예외가_발생한다() {
            // given
            String code = null;

            // when & then
            assertThatThrownBy(() -> new LessonCode(code))
                    .isInstanceOf(LessonDomainException.class)
                    .hasMessage("수업 코드가 비어있습니다.");
        }

        @Test
        void 코드가_공백이면_예외가_발생한다() {
            // given
            String code = "   ";

            // when & then
            assertThatThrownBy(() -> new LessonCode(code))
                    .isInstanceOf(LessonDomainException.class)
                    .hasMessage("수업 코드가 비어있습니다.");
        }
    }
}
