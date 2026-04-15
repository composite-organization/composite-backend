package kr.composite.api.vote.ui.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import kr.composite.api.vote.domain.VoteOption;
import kr.composite.api.vote.domain.VoteSubmissions;

@Schema(description = "투표 종료 결과 데이터 (ENDED 상태인 경우에만 포함됩니다)")
public record VoteEndedResponse(

        @Schema(description = "최다 득표 선택지 ID 목록 (공동 1위 포함)")
        List<Long> selectedOptionIds
) {

    public static VoteEndedResponse from(VoteSubmissions voteSubmissions) {
        long maxCount = voteSubmissions.maxSubmissionCount();

        List<Long> selectedOptionIds = voteSubmissions.getVoteOptions().stream()
                .filter(option -> maxCount > 0)
                .filter(option -> voteSubmissions.countSubmissions(option) == maxCount)
                .map(VoteOption::getId)
                .toList();

        return new VoteEndedResponse(selectedOptionIds);
    }
}
