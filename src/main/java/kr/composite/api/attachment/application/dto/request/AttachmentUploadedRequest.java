package kr.composite.api.attachment.application.dto.request;

public record AttachmentUploadedRequest(
        String name,
        Long size,
        String key
) {

}
