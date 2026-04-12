package kr.composite.api.lesson.ui.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "수업 참여 요청")
public record JoinLessonRequest(
        @Schema(description = "참여자 이름 (null일 경우 사용자 이름 사용)", example = "홍길동")
        String name
) {

}
