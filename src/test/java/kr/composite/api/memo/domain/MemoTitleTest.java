package kr.composite.api.memo.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class MemoTitleTest {

    @Test
    void 유효한_제목으로_메모_제목을_생성할_수_있다() {
        // given
        String title = "메모테스트";

        // when
        MemoTitle memoTitle = new MemoTitle(title);

        // then
        assertThat(memoTitle).isNotNull();
        assertThat(memoTitle.getValue()).isEqualTo(title);
    }

    @Nested
    @DisplayName("메모 제목 길이 검증")
    class LengthValidation {

        @Test
        void 제목이_최대_길이_이하이면_메모_제목을_생성할_수_있다() {
            // given
            String title = "a".repeat(50);

            // when & then
            assertThatCode(() -> new MemoTitle(title)).doesNotThrowAnyException();
        }

        @Test
        void 제목이_최대_길이를_초과하면_예외가_발생한다() {
            // given
            String title = "a".repeat(51);

            // when & then
            assertThatThrownBy(() -> new MemoTitle(title)).isInstanceOf(MemoDomainException.class);
        }
    }
}
