package kr.composite.api.memo.ui.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import kr.composite.api.memo.application.dto.request.MemoWidgetUpdateRequest;

@Schema(description = "메모 위젯 수정 요청")
public record UpdateMemoWidgetRequest(

        @NotNull
        @Schema(description = "제목", example = "메모 제목")
        String title,

        @NotNull
        @Schema(description = "내용", example = "메모 내용")
        String content
) {

    public MemoWidgetUpdateRequest toMemoWidgetUpdateRequest(Long memoWidgetId) {
        return new MemoWidgetUpdateRequest(memoWidgetId, title, content);
    }
}
