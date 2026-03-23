package kr.composite.api.attachment.application.dto.response;

import kr.composite.api.attachment.domain.Attachment;

public record AttachmentMetaDataResponse(
        Long id,
        Long attachmentWidgetId,
        String name,
        Double size,
        String unit
) {

    public static AttachmentMetaDataResponse from(Attachment attachment) {
        return new AttachmentMetaDataResponse(attachment.getId(),
                attachment.getAttachmentWidgetId(),
                attachment.getAttachmentName().getValue(),
                attachment.getAttachmentSize().getFormattedSize(),
                attachment.getUnit().name()
        );
    }
}
