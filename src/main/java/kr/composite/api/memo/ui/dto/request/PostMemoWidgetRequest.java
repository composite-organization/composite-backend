package kr.composite.api.memo.ui.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.composite.api.memo.application.dto.request.MemoWidgetAddRequest;

@Schema(description = "메모 위젯 생성 요청")
public record PostMemoWidgetRequest(
        @Schema(description = "수업 ID", example = "1")
        Long lessonId,

        @Schema(description = "제목", example = "메모 제목")
        String title,

        @Schema(description = "내용", example = "메모 내용")
        String content
) {

    public MemoWidgetAddRequest toMemoWidgetAddRequest() {
        return new MemoWidgetAddRequest(lessonId, title, content);
    }
}
