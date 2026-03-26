package kr.composite.api.attachment.application.dto.response;

public record AttachmentUriResponse(
        String presignedUrl
) {

    public static AttachmentUriResponse from(String uri) {
        return new AttachmentUriResponse(uri);
    }
}
