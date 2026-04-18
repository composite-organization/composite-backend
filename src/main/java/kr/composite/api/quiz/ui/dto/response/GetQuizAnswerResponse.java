package kr.composite.api.quiz.ui.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import kr.composite.api.quiz.domain.QuizOption;

@Schema(description = "퀴즈 정답 조회 응답")
public record GetQuizAnswerResponse(
        @Schema(description = "퀴즈 정답 ID List", example = "[1, 2, 3]")
        List<Long> answerQuizOptionIds
) {

    public static GetQuizAnswerResponse from(List<QuizOption> answerQuizOptions) {
        List<Long> answerQuizOptionIds = answerQuizOptions
                .stream()
                .map(quizOption -> quizOption.getId())
                .toList();

        return new GetQuizAnswerResponse(answerQuizOptionIds);
    }
}
