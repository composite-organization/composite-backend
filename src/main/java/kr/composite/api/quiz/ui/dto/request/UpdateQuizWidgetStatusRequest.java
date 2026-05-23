package kr.composite.api.quiz.ui.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "퀴즈 위젯 상태 업데이트 요청")
public record UpdateQuizWidgetStatusRequest(
        @NotNull
        @Schema(description = "퀴즈 위젯 상태", example = "시작 전,진행 중,종료")
        String status
) {

}
