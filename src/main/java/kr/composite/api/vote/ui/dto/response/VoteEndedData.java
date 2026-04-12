package kr.composite.api.vote.ui.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.Map;
import kr.composite.api.vote.domain.VoteOption;
import kr.composite.api.vote.domain.VoteSubmissions;

@Schema(description = "투표 결과 데이터 (ENDED 상태)")
public record VoteEndedData(

        @Schema(description = "각 선택지별 결과")
        List<OptionResult> options,

        @Schema(description = "최종 선정된 선택지 ID 목록")
        List<Long> selectedOptionIds
) implements VoteStatusData {

    public static VoteEndedData of(List<VoteOption> voteOptions, VoteSubmissions voteSubmissions) {
        Map<VoteOption, Long> submissionCountByOption = voteSubmissions.countByOptions(voteOptions);

        long maxCount = submissionCountByOption.values().stream()
                .mapToLong(Long::longValue)
                .max()
                .orElse(0L);

        List<OptionResult> optionResults = voteOptions.stream()
                .map(option -> {
                    long count = submissionCountByOption.getOrDefault(option, 0L);

                    return new OptionResult(
                            option.getId(),
                            option.getContent().getValue(),
                            count,
                            maxCount > 0 && count == maxCount
                    );
                })
                .toList();

        List<Long> selectedIds = optionResults.stream()
                .filter(OptionResult::isSelected)
                .map(OptionResult::optionId)
                .toList();

        return new VoteEndedData(optionResults, selectedIds);
    }

    @Schema(description = "선택지별 결과")
    public record OptionResult(

            @Schema(description = "선택지 ID", example = "1")
            Long optionId,

            @Schema(description = "선택지 내용", example = "칼국수")
            String content,

            @Schema(description = "총 투표 인원 수", example = "5")
            long count,

            @Schema(description = "최종 선정 여부", example = "true")
            boolean isSelected
    ) {

    }
}
