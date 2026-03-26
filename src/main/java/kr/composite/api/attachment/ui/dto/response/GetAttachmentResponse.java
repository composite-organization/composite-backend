package kr.composite.api.attachment.ui.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.composite.api.attachment.application.dto.response.AttachmentUriResponse;

@Schema(description = "수업자료 조회 응답")
public record GetAttachmentResponse(

        @Schema(description = "수업자료 파일 다운로드 url", example = "s3amazon.com")
        String url
) {

    public static GetAttachmentResponse from(AttachmentUriResponse attachmentUriResponse) {
        return new GetAttachmentResponse(attachmentUriResponse.presignedUrl());
    }
}
