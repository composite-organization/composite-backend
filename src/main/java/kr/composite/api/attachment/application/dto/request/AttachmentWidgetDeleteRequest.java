package kr.composite.api.attachment.application.dto.request;

public record AttachmentWidgetDeleteRequest(
        Long id
) {

    public static AttachmentWidgetDeleteRequest from(Long attachmentWidgetId) {
        return new AttachmentWidgetDeleteRequest(attachmentWidgetId);
    }
}
