package kr.composite.api.vote.ui.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@Schema(description = "투표 위젯 생성 요청")
public record PostVoteWidgetRequest(

        @NotNull
        @Schema(description = "수업 ID", example = "1")
        Long lessonId,

        @NotNull
        @Schema(description = "투표 안건", example = "오늘 밥 뭐 드셨나요?")
        String title,

        @NotNull
        @Schema(description = "투표 선택지 목록", example = "[\"칼국수\", \"비빔밥\", \"돈까스\"]")
        List<String> options,

        @NotNull
        @Schema(description = "익명 투표 여부", example = "false")
        Boolean isAnonymous,

        @NotNull
        @Schema(description = "복수 선택 여부", example = "false")
        Boolean isMultiSelectable
) {

}
