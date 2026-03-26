package kr.composite.api.attachment.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class AttachmentNameTest {

    @Test
    void 유효한_이름으로_자료_이름을생성할_수_있다() {
        // given
        String name = "자료 이름";

        // when
        AttachmentName attachmentName = new AttachmentName(name);

        // then
        assertThat(attachmentName).isNotNull();
        assertThat(attachmentName.getValue()).isEqualTo(name);
    }

    @Nested
    @DisplayName("자료 이름 길이 검증")
    class LengthValidation {

        @Test
        void 제목이_최소_길이_이상이면_자료_이름을_생성할_수_있다() {
            // given
            String name = "a".repeat(5);

            // when & then
            assertThatCode(() -> new AttachmentName(name)).doesNotThrowAnyException();
        }

        @Test
        void 제목이_최대_길이를_초과하면_예외가_발생한다() {
            // given
            String name = "a".repeat(0);

            // when & then
            assertThatThrownBy(() -> new AttachmentName(name)).isInstanceOf(AttachmentDomainException.class);
        }
    }
}
