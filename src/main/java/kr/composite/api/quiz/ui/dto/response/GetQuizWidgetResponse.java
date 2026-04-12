package kr.composite.api.quiz.ui.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import kr.composite.api.quiz.domain.QuizOption;
import kr.composite.api.quiz.domain.QuizWidget;

@Schema(description = "퀴즈 위젯 조회 응답")
public record GetQuizWidgetResponse(
        @Schema(description = "퀴즈 위젯 ID", example = "1")
        Long quizWidgetId,

        @Schema(description = "퀴즈 제목", example = "제일 웃음이 많은 사람은?")
        String title,

        @Schema(description = "퀴즈 상태", example = "진행 중")
        String status,

        @Schema(description = "정답률 (%)", example = "75") // 추가
        int correctRate,

        @Schema(description = "퀴즈 선택지")
        List<QuizOptionResponse> options
) {

    public static GetQuizWidgetResponse of(QuizWidget quizWidget, List<QuizOption> quizOptions, int correctRate) {
        return new GetQuizWidgetResponse(
                quizWidget.getId(),
                quizWidget.getTitle().getValue(),
                quizWidget.getQuizStatus().getDescription(),
                correctRate, // 추가
                quizOptions.stream()
                        .map(QuizOptionResponse::from)
                        .toList()
        );
    }

    public record QuizOptionResponse(
            @Schema(description = "퀴즈 선택지 ID", example = "1")
            Long quizOptionId,

            @Schema(description = "퀴즈 선택지 내용", example = "슬링키")
            String content
    ) {

        public static QuizOptionResponse from(QuizOption quizOption) {
            return new QuizOptionResponse(
                    quizOption.getId(),
                    quizOption.getContent()
            );
        }
    }
}
