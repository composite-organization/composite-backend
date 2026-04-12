package kr.composite.api.lesson.ui.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "수업 생성 요청")
public record CreateLessonRequest(
        @NotNull
        @Schema(description = "수업자 이름", example = "김민기")
        String teacherName,

        @NotNull
        @Schema(description = "수업 이름", example = "공업 수학")
        String lessonName,

        @NotNull
        @Schema(description = "수업 코드", example = "ek2j2")
        String lessonCode,

        @NotNull
        @Schema(description = "수업 비밀번호", example = "password")
        String password
) {

}
