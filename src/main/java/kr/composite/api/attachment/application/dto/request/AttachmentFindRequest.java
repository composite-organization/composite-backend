package kr.composite.api.attachment.application.dto.request;

public record AttachmentFindRequest(
        Long attachmentId,
        Long attachmentWidgetId
) {

    public static AttachmentFindRequest of(Long attachmentId, Long attachmentWidgetId) {
        return new AttachmentFindRequest(attachmentId,attachmentWidgetId);
    }
}
