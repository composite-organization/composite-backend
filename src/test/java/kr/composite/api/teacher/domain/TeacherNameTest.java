package kr.composite.api.teacher.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class TeacherNameTest {

    @Test
    void 유효한_이름으로_수업자_이름을_생성할_수_있다() {
        // given
        String name = "김선생";

        // when
        TeacherName teacherName = new TeacherName(name);

        // then
        assertThat(teacherName).isNotNull();
        assertThat(teacherName.getValue()).isEqualTo(name);
    }

    @Nested
    @DisplayName("수업자 이름 검증")
    class Validation {

        @Test
        void 이름이_비어있으면_예외가_발생한다() {
            // given
            String name = "";

            // when & then
            assertThatThrownBy(() -> new TeacherName(name))
                    .isInstanceOf(TeacherDomainException.class)
                    .hasMessage("수업자 이름이 비어있습니다.");
        }

        @Test
        void 이름이_공백이면_예외가_발생한다() {
            // given
            String name = "   ";

            // when & then
            assertThatThrownBy(() -> new TeacherName(name))
                    .isInstanceOf(TeacherDomainException.class)
                    .hasMessage("수업자 이름이 비어있습니다.");
        }

        @Test
        void 이름이_최대_길이를_초과하면_예외가_발생한다() {
            // given
            String name = "a".repeat(51);

            // when & then
            assertThatThrownBy(() -> new TeacherName(name))
                    .isInstanceOf(TeacherDomainException.class)
                    .hasMessageContaining("수업자 이름의 길이가 유효하지 않습니다.");
        }

        @Test
        void 이름이_유효한_길이_범위이면_성공한다() {
            // given
            String name = "a".repeat(50);

            // when & then
            assertThatCode(() -> new TeacherName(name)).doesNotThrowAnyException();
        }
    }
}
