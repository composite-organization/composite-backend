package kr.composite.api.attachment.application.dto.request;

public record AttachmentWidgetFindRequest(
        Long id
) {

    public static AttachmentWidgetFindRequest from(Long id) {
        return new AttachmentWidgetFindRequest(id);
    }
}
