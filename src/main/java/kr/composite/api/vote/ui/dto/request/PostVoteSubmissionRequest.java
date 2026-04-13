package kr.composite.api.vote.ui.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import kr.composite.api.vote.domain.VoteSubmission;

@Schema(description = "투표 제출 요청")
public record PostVoteSubmissionRequest(

        @NotNull
        @Schema(description = "선택한 옵션 ID 목록", example = "[1, 2]")
        List<Long> optionIds
) {

    public List<VoteSubmission> toVoteSubmissions(Long voteWidgetId, Long studentId) {
        return optionIds.stream()
                .map(optionId -> new VoteSubmission(studentId, voteWidgetId, optionId))
                .toList();
    }

    public boolean hasEmptyOptionIds() {
        return optionIds.isEmpty();
    }

    public boolean hasMultipleOptionIds() {
        return optionIds.size() > 1;
    }
}
