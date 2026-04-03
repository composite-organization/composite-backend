package kr.composite.api.quiz.ui.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@Schema(description = "퀴즈 선택지 제출 요청")
public record SubmitQuizSubmissionRequest(
        @NotNull
        @Schema(description = "퀴즈 선택지 ID List", example = "[1, 2, 3]")
        List<Long> quizOptionIds
) {

}
