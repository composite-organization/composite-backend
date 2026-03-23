package kr.composite.api.attachment.application.dto.response;

public record AttachmentUrlResponse(
        String presignedUrl
) {

    public static AttachmentUrlResponse from(String presignedUrl) {
        return new AttachmentUrlResponse(presignedUrl);
    }
}
