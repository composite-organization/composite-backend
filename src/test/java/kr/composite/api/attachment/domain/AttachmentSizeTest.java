package kr.composite.api.attachment.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class AttachmentSizeTest {

    @Test
    void 유효한_사이즈라면_객체를_성공적으로_생성할_수_있다() {
        // given
        Long sizeValue = 1024L;

        // when & then
        assertThatCode(() -> new AttachmentSize(sizeValue))
                .doesNotThrowAnyException();
    }

    @Nested
    @DisplayName("포맷팅된 사이즈 값 계산 검증")
    class FormattedSizeValidation {

        @Test
        void MB_단위일_때_올바른_소수점_포맷팅_값을_반환할_수_있다() {
            // given
            long twoMegaByte = 2L * 1024L * 1024L;
            AttachmentSize size = new AttachmentSize(twoMegaByte);
            AttachmentUnit attachmentUnit = AttachmentUnit.getAppropriateUnit(twoMegaByte);

            // when
            double formattedSize = size.getFormattedSize(attachmentUnit.getByteSize());

            // then
            assertThat(formattedSize).isEqualTo(2.0);
        }

        @Test
        void KB_단위일_때_올바른_소수점_포맷팅_값을_반환할_수_있다() {
            // given
            long halfKiloByte = 512L;
            AttachmentSize size = new AttachmentSize(halfKiloByte);
            AttachmentUnit attachmentUnit = AttachmentUnit.getAppropriateUnit(halfKiloByte);

            // when
            double formattedSize = size.getFormattedSize(attachmentUnit.getByteSize());

            // then
            // 512 / 1024 = 0.5
            assertThat(formattedSize).isEqualTo(0.5);
        }
    }
}
