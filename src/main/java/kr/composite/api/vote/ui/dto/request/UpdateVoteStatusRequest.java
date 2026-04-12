package kr.composite.api.vote.ui.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "투표 상태 변경 요청")
public record UpdateVoteStatusRequest(

        @NotNull
        @Schema(description = "변경할 상태 (IN_PROGRESS, ENDED)", example = "IN_PROGRESS")
        String status
) {

}
