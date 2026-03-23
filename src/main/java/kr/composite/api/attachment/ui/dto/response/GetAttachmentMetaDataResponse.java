package kr.composite.api.attachment.ui.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.composite.api.attachment.application.dto.response.AttachmentMetaDataResponse;

@Schema(description = "수업자료 메타데이터 조회 응답")
public record GetAttachmentMetaDataResponse(

        @Schema(description = "수업자료 위젯 ID", example = "1")
        Long id,

        @Schema(description = "수업자료 위젯 ID", example = "1")
        Long attachmentWidgetId,

        @Schema(description = "수업자료 이름", example = "file.jpg")
        String name,

        @Schema(description = "수업자료 파일 크기", example = "1.5")
        Double size,

        @Schema(description = "수업자료 파일 단위", example = "MB")
        String unit
) {

    public static GetAttachmentMetaDataResponse from(AttachmentMetaDataResponse attachmentMetaDataResponse) {
        return new GetAttachmentMetaDataResponse(attachmentMetaDataResponse.id(),
                attachmentMetaDataResponse.attachmentWidgetId(),
                attachmentMetaDataResponse.name(),
                attachmentMetaDataResponse.size(),
                attachmentMetaDataResponse.unit());
    }
}
