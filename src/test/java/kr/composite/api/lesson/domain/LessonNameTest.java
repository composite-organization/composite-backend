package kr.composite.api.lesson.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class LessonNameTest {

    @Test
    void 유효한_이름으로_수업_이름을_생성할_수_있다() {
        // given
        String name = "자바 프로그래밍";

        // when
        LessonName lessonName = new LessonName(name);

        // then
        assertThat(lessonName).isNotNull();
        assertThat(lessonName.getValue()).isEqualTo(name);
    }

    @Nested
    @DisplayName("수업 이름 검증")
    class Validation {

        @Test
        void 이름이_null이면_예외가_발생한다() {
            // given
            String name = null;

            // when & then
            assertThatThrownBy(() -> new LessonName(name))
                    .isInstanceOf(LessonDomainException.class)
                    .hasMessage("수업 이름이 비어있습니다.");
        }

        @Test
        void 이름이_최소_길이보다_짧으면_예외가_발생한다() {
            // given
            String name = "";

            // when & then
            assertThatThrownBy(() -> new LessonName(name))
                    .isInstanceOf(LessonDomainException.class)
                    .hasMessageContaining("수업 이름의 길이가 유효하지 않습니다.");
        }

        @Test
        void 이름이_최대_길이를_초과하면_예외가_발생한다() {
            // given
            String name = "a".repeat(21);

            // when & then
            assertThatThrownBy(() -> new LessonName(name))
                    .isInstanceOf(LessonDomainException.class)
                    .hasMessageContaining("수업 이름의 길이가 유효하지 않습니다.");
        }

        @Test
        void 이름이_유효한_길이_범위이면_성공한다() {
            // given
            String name = "a".repeat(20);

            // when & then
            assertThatCode(() -> new LessonName(name)).doesNotThrowAnyException();
        }
    }
}
