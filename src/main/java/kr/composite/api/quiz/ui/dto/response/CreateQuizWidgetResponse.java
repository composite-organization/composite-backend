package kr.composite.api.quiz.ui.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "퀴즈 위젯 생성 응답")
public record CreateQuizWidgetResponse(
        @Schema(description = "퀴즈 위젯 ID", example = "1")
        Long quizWidgetId
) {

    public static CreateQuizWidgetResponse from(Long quizWidgetId) {
        return new CreateQuizWidgetResponse(quizWidgetId);
    }
}
