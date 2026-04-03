package kr.composite.api.quiz.ui.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@Schema(description = "퀴즈 위젯 생성 요청")
public record CreateQuizWidgetRequest(
        @NotNull
        @Schema(description = "수업 ID", example = "1")
        Long lessonId,

        @NotNull
        @Schema(description = "퀴즈 위젯 제목", example = "가장 웃음이 많은 사람은?")
        String title,

        @NotNull
        @Schema(description = "퀴즈 선택지")
        List<QuizOptionRequest> options
) {

    public record QuizOptionRequest(

            @NotNull
            @Schema(description = "퀴즈 선택지 내용", example = "슬링키")
            String content,

            @NotNull
            @Schema(description = "퀴즈 선택지 정답 여부", example = "true/false")
            boolean isCorrect
    ) {

    }
}
