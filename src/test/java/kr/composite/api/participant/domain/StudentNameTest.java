package kr.composite.api.participant.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kr.composite.api.student.domain.StudentDomainException;
import kr.composite.api.student.domain.StudentName;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class StudentNameTest {

    @Test
    void 유효한_이름으로_학생_이름을_생성할_수_있다() {
        // given
        String name = "홍길동";

        // when
        StudentName studentName = new StudentName(name);

        // then
        assertThat(studentName).isNotNull();
        assertThat(studentName.getValue()).isEqualTo(name);
    }

    @Nested
    @DisplayName("학생 이름 검증")
    class Validation {

        @Test
        void 이름이_비어있으면_예외가_발생한다() {
            // given
            String name = "";

            // when & then
            assertThatThrownBy(() -> new StudentName(name))
                    .isInstanceOf(StudentDomainException.class)
                    .hasMessage("학생 이름이 비어있습니다.");
        }

        @Test
        void 이름이_공백이면_예외가_발생한다() {
            // given
            String name = "   ";

            // when & then
            assertThatThrownBy(() -> new StudentName(name))
                    .isInstanceOf(StudentDomainException.class)
                    .hasMessage("학생 이름이 비어있습니다.");
        }

        @Test
        void 이름이_최대_길이를_초과하면_예외가_발생한다() {
            // given
            String name = "a".repeat(51);

            // when & then
            assertThatThrownBy(() -> new StudentName(name))
                    .isInstanceOf(StudentDomainException.class)
                    .hasMessageContaining("학생 이름의 길이가 유효하지 않습니다.");
        }

        @Test
        void 이름이_유효한_길이_범위이면_성공한다() {
            // given
            String name = "a".repeat(50);

            // when & then
            assertThatCode(() -> new StudentName(name)).doesNotThrowAnyException();
        }
    }
}
