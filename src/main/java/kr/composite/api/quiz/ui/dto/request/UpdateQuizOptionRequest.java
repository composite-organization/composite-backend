package kr.composite.api.quiz.ui.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@Schema(description = "퀴즈 선택지 수정 요청")
public record UpdateQuizOptionRequest(
        Long quizWidgetId,
        List<QuizOptionRequest> options
) {

    public record QuizOptionRequest(
            @Schema(description = "퀴즈 선택지 ID", example = "1/null")
            Long quizOptionId,

            @NotNull
            @Schema(description = "퀴즈 선택지 내용", example = "머랭")
            String content,

            @NotNull
            @Schema(description = "퀴즈 선택지 정답 여부", example = "true/false")
            boolean isCorrect
    ) {

    }
}
