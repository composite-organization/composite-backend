package kr.composite.api.quiz.ui.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "퀴즈 정답률 조회 응답")
public record GetQuizResultResponse(
        @Schema(description = "퀴즈 정답률", example = "50")
        Integer correctRate
) {

    public static GetQuizResultResponse from(int correctRate) {
        return new GetQuizResultResponse(correctRate);
    }
}
