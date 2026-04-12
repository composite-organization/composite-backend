package kr.composite.api.vote.ui.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import kr.composite.api.vote.domain.VoteSubmissions;

@Schema(description = "투표 종료 결과 데이터 (ENDED 상태인 경우에만 포함됩니다)")
public record VoteEndedResponse(

        @Schema(description = "선택지별 선정 결과 목록")
        List<OptionResult> optionResults
) {

    public static VoteEndedResponse from(VoteSubmissions voteSubmissions) {
        long maxCount = voteSubmissions.maxSubmissionCount();

        List<OptionResult> optionResults = voteSubmissions.getVoteOptions().stream()
                .map(option -> {
                    long count = voteSubmissions.countSubmissions(option);

                    return new OptionResult(option.getId(), maxCount > 0 && count == maxCount);
                })
                .toList();

        return new VoteEndedResponse(optionResults);
    }

    @Schema(description = "선택지별 결과")
    public record OptionResult(

            @Schema(description = "선택지 ID", example = "1")
            Long optionId,

            @Schema(description = "최다 득표 선택지 여부 (공동 1위 포함)", example = "true")
            boolean isSelected
    ) {

    }
}
