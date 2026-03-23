package kr.composite.api.attachment.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.Test;

class AttachmentTest {

    @Test
    void 유효한_값으로_자료를_생성할_수_있다() {
        // given
        Long widgetId = 1L;
        String key = "unique-attachment-key";
        AttachmentName name = new AttachmentName("test-file.pdf");
        AttachmentSize size = new AttachmentSize(2048L);
        AttachmentUnit unit = AttachmentUnit.KB;

        // when
        Attachment attachment = new Attachment(widgetId, key, name, size, unit);

        // then
        assertAll(
                () -> assertThat(attachment.getAttachmentWidgetId()).isEqualTo(widgetId),
                () -> assertThat(attachment.getAttachmentKey()).isEqualTo(key),
                () -> assertThat(attachment.getAttachmentName()).isEqualTo(name),
                () -> assertThat(attachment.getAttachmentSize()).isEqualTo(size),
                () -> assertThat(attachment.getUnit()).isEqualTo(unit)
        );
    }
}
