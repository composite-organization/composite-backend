package kr.composite.api.attachment.application.dto.request;

public record AttachmentDeleteRequest(
        Long attachmentId,
        Long attachmentWidgetId
) {

    public static AttachmentDeleteRequest of(Long attachmentId, Long attachmentWidgetId) {
        return new AttachmentDeleteRequest(attachmentId, attachmentWidgetId);
    }
}
