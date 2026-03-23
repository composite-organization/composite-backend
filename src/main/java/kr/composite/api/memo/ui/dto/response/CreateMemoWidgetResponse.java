package kr.composite.api.memo.ui.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import kr.composite.api.memo.application.dto.response.MemoWidgetResponse;

@Schema(description = "메모 위젯 생성 응답")
public record CreateMemoWidgetResponse(
        @Schema(description = "메모 위젯 ID", example = "1")
        Long id,

        @Schema(description = "위젯 ID", example = "1")
        Long widgetId,

        @Schema(description = "메모 제목", example = "회의록")
        String title,

        @Schema(description = "메모 내용", example = "내일 오전 10시 팀 미팅")
        String content,

        @Schema(
                description = "수정 시간",
                example = "2026-03-19 14:30",
                pattern = "yyyy-MM-dd HH:mm"
        )
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        LocalDateTime updatedTime
) {

    public static CreateMemoWidgetResponse from(MemoWidgetResponse response) {
        return new CreateMemoWidgetResponse(
                response.id(),
                response.widgetId(),
                response.title(),
                response.content(),
                response.updatedTime()
        );
    }
}
