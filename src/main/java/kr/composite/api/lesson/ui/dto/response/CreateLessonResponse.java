package kr.composite.api.lesson.ui.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "수업 생성 응답")
public record CreateLessonResponse(
        @Schema(description = "수업 ID", example = "1")
        Long lessonId,
        @Schema(description = "수업 이름", example = "공업수학")
        String lessonName
) {

}
