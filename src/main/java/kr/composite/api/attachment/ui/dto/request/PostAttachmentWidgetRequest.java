package kr.composite.api.attachment.ui.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import kr.composite.api.attachment.application.dto.request.AttachmentWidgetAddRequest;

@Schema(description = "수업자료 위젯 생성 요청")
public record PostAttachmentWidgetRequest(
        @NotNull
        @Schema(description = "수업 ID")
        Long lessonId
) {

    public AttachmentWidgetAddRequest toMemoWidgetAddRequest() {
        return new AttachmentWidgetAddRequest(lessonId);
    }
}
