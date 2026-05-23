package kr.composite.api.attachment.application.dto.response;

public record AttachmentUriResponse(
        String presignedUrl,
        String fileName
) {

    public static AttachmentUriResponse from(String uri, String fileName) {
        return new AttachmentUriResponse(uri, fileName);
    }
}
