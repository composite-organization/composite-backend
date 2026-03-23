package kr.composite.api.attachment.ui.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.composite.api.attachment.application.dto.response.AttachmentWidgetResponse;

@Schema(description = "수업자료 위젯 조회 응답")
public record GetAttachmentWidgetResponse(

        @Schema(description = "수업자료 위젯 ID", example = "1")
        Long id,

        @Schema(description = "위젯 ID", example = "1")
        Long widgetId
) {

    public static GetAttachmentWidgetResponse from(AttachmentWidgetResponse attachmentWidgetResponse) {
        return new GetAttachmentWidgetResponse(
                attachmentWidgetResponse.id(),
                attachmentWidgetResponse.widgetId());
    }
}
