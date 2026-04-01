package kr.composite.api.lesson.ui.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "수업 생성 요청")
public record FindMyLessonRequest(
        @NotNull
        @Schema(description = "수업 코드", example = "S12BC")
        String lessonCode,

        @NotNull
        @Schema(description = "수업 비밀번호", example = "password")
        String password
) {

}
