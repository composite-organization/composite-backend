package kr.composite.api.lesson.ui.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "학생 수업 참여 응답")
public record JoinStudentResponse(

        @Schema(description = "수업 ID", example = "1")
        Long lessonId
) {

    public static JoinStudentResponse from(Long lessonId) {
        return new JoinStudentResponse(lessonId);
    }
}
