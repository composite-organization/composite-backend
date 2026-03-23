package kr.composite.api.attachment.application.dto.response;

import kr.composite.api.attachment.domain.AttachmentWidget;

public record AttachmentWidgetResponse(
        Long id,
        Long widgetId
) {

    public static AttachmentWidgetResponse from(AttachmentWidget attachmentWidget) {
        return new AttachmentWidgetResponse(attachmentWidget.getId(), attachmentWidget.getWidgetId());
    }
}
