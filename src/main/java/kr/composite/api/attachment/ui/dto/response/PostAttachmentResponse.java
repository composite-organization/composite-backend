package kr.composite.api.attachment.ui.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.composite.api.attachment.application.dto.response.AttachmentResponse;

@Schema(description = "수업자료 위젯 생성 응답")
public record PostAttachmentResponse(

        @Schema(description = "수업자료 ID", example = "1")
        Long id,

        @Schema(description = "수업자료 위젯 ID", example = "1")
        Long attachmentWidgetId,

        @Schema(description = "수업자료 이름", example = "file.jpg")
        String name,

        @Schema(description = "수업자료 크기", example = "10")
        Double size,

        @Schema(description = "수업자료 단위", example = "MB")
        String unit
) {

    public static PostAttachmentResponse from(AttachmentResponse attachmentResponse) {
        return new PostAttachmentResponse(
                attachmentResponse.id(),
                attachmentResponse.attachmentWidgetId(),
                attachmentResponse.name(),
                attachmentResponse.size(),
                attachmentResponse.unit()
        );
    }
}
