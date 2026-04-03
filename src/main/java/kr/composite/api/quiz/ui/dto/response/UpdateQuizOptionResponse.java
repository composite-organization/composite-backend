package kr.composite.api.quiz.ui.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "퀴즈 선택지 업데이트 응답")
public record UpdateQuizOptionResponse(
        @Schema(description = "퀴즈 선택지 ID", example = "1")
        Long quizWidgetId
) {

    public static UpdateQuizOptionResponse from(Long quizWidgetId) {
        return new UpdateQuizOptionResponse(quizWidgetId);
    }
}
