package kr.composite.api.lesson.ui.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "수업 인증 응답")
public record AuthenticateLessonResponse(

        @Schema(description = "수업 ID", example = "1")
        Long lessonId
) {

    public static AuthenticateLessonResponse from(Long lessonId) {
        return new AuthenticateLessonResponse(lessonId);
    }
}
