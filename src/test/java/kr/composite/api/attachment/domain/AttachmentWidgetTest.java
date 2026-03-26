package kr.composite.api.attachment.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class AttachmentWidgetTest {

    @Test
    void 유효한_값으로_자료_공유_위젯을_생성할_수_았다() {
        // given
        Long widgetId = 100L;

        // when
        AttachmentWidget attachmentWidget = new AttachmentWidget(widgetId);

        // then
        assertThat(attachmentWidget).isNotNull();
        assertThat(attachmentWidget.getWidgetId()).isEqualTo(widgetId);
    }
}
