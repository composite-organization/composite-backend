package kr.composite.api.lesson.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class LessonPasswordTest {

    @Test
    void 유효한_비밀번호로_수업_비밀번호를_생성할_수_있다() {
        // given
        String password = "password123";

        // when
        LessonPassword lessonPassword = new LessonPassword(password);

        // then
        assertThat(lessonPassword).isNotNull();
    }

    @Nested
    @DisplayName("수업 비밀번호 검증")
    class Validation {

        @Test
        void 비밀번호가_null이면_예외가_발생한다() {
            // given
            String password = null;

            // when & then
            assertThatThrownBy(() -> new LessonPassword(password))
                    .isInstanceOf(LessonDomainException.class)
                    .hasMessage("수업 비밀번호가 비어있습니다.");
        }

        @Test
        void 비밀번호가_최소_길이보다_짧으면_예외가_발생한다() {
            // given
            String password = "";

            // when & then
            assertThatThrownBy(() -> new LessonPassword(password))
                    .isInstanceOf(LessonDomainException.class)
                    .hasMessageContaining("수업 비밀번호의 길이가 유효하지 않습니다.");
        }

        @Test
        void 비밀번호가_최대_길이를_초과하면_예외가_발생한다() {
            // given
            String password = "a".repeat(21);

            // when & then
            assertThatThrownBy(() -> new LessonPassword(password))
                    .isInstanceOf(LessonDomainException.class)
                    .hasMessageContaining("수업 비밀번호의 길이가 유효하지 않습니다.");
        }

        @Test
        void 비밀번호에_영문숫자_외의_문자가_포함되면_예외가_발생한다() {
            // given
            String password = "pass!";

            // when & then
            assertThatThrownBy(() -> new LessonPassword(password))
                    .isInstanceOf(LessonDomainException.class)
                    .hasMessage("수업 비밀번호 형식이 유효하지 않습니다.");
        }

        @Test
        void 비밀번호가_유효한_형식이면_성공한다() {
            // given
            String password = "validPassword123";

            // when & then
            assertThatCode(() -> new LessonPassword(password)).doesNotThrowAnyException();
        }
    }

    @Nested
    @DisplayName("비밀번호 확인")
    class PasswordCheck {

        @Test
        void 비밀번호가_일치하면_성공한다() {
            // given
            String password = "password123";
            LessonPassword lessonPassword = new LessonPassword(password);

            // when & then
            assertThatCode(() -> lessonPassword.checkPassword(password)).doesNotThrowAnyException();
        }

        @Test
        void 비밀번호가_일치하지_않으면_예외가_발생한다() {
            // given
            LessonPassword lessonPassword = new LessonPassword("password123");

            // when & then
            assertThatThrownBy(() -> lessonPassword.checkPassword("wrongPassword"))
                    .isInstanceOf(LessonDomainException.class)
                    .hasMessage("수업 비밀번호가 일치하지 않습니다.");
        }
    }
}
