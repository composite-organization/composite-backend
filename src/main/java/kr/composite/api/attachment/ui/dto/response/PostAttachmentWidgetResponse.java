package kr.composite.api.attachment.ui.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.composite.api.attachment.application.dto.response.AttachmentWidgetResponse;

@Schema(description = "수업자료 위젯 생성 응답")
public record PostAttachmentWidgetResponse(

        @Schema(description = "수업자료 위젯 ID", example = "1")
        Long id,

        @Schema(description = "수업 ID", example = "1")
        Long lessonId
) {

    public static PostAttachmentWidgetResponse from(AttachmentWidgetResponse attachmentWidgetResponse) {
        return new PostAttachmentWidgetResponse(attachmentWidgetResponse.id(), attachmentWidgetResponse.widgetId());
    }
}
