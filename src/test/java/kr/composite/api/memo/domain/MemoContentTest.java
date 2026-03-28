package kr.composite.api.memo.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class MemoContentTest {

    @Test
    void 유효한_내용으로_메모_내용을_생성할_수_있다() {
        // given
        String content = "메모내용";

        // when
        MemoContent memoContent = new MemoContent(content);

        // then
        assertThat(memoContent).isNotNull();
        assertThat(memoContent.getValue()).isEqualTo(content);
    }

    @Nested
    @DisplayName("메모 내용 길이 검증")
    class LengthValidation {

        @Test
        void 내용이_최대_길이_이하이면_메모_내용을_생성할_수_있다() {
            // given
            String content = "a".repeat(200);

            // when & then
            assertThatCode(() -> new MemoContent(content))
                    .doesNotThrowAnyException();
        }

        @Test
        void 내용이_최대_길이를_초과하면_예외가_발생한다() {
            // given
            String content = "a".repeat(201);

            // when & then
            assertThatThrownBy(() -> new MemoContent(content)).isInstanceOf(MemoDomainException.class);
        }
    }
}
