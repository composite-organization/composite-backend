package kr.composite.api.quiz.ui.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import kr.composite.api.quiz.domain.QuizSubmission;

@Schema(description = "퀴즈 선택지 제출 요청")
public record SubmitQuizSubmissionRequest(
        @NotNull
        @Schema(description = "퀴즈 선택지 ID List", example = "[1, 2, 3]")
        List<Long> quizOptionIds
) {

    public List<QuizSubmission> toQuizSubmissions(Long studentId, Long quizWidgetId) {
        return quizOptionIds.stream()
                .map(optionId -> new QuizSubmission(studentId, quizWidgetId, optionId))
                .toList();
    }
}
