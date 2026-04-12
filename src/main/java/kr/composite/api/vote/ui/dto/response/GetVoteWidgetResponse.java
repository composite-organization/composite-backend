package kr.composite.api.vote.ui.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import kr.composite.api.vote.domain.VoteOption;
import kr.composite.api.vote.domain.VoteWidget;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "투표 위젯 조회 응답 (상태에 따라 data 필드의 구조가 달라집니다)")
public record GetVoteWidgetResponse(

        @Schema(description = "투표 위젯 ID", example = "1")
        Long id,

        @Schema(description = "위젯 ID", example = "1")
        Long widgetId,

        @Schema(description = "투표 안건", example = "오늘 밥 뭐 드셨나요?")
        String title,

        @Schema(description = "익명 투표 여부", example = "false")
        boolean isAnonymous,

        @Schema(description = "복수 선택 여부", example = "false")
        boolean isMultiSelectable,

        @Schema(description = "투표 상태 (IN_PROGRESS, ENDED)", example = "IN_PROGRESS")
        String status,

        @Schema(description = "투표 선택지 목록")
        List<OptionResponse> options,

        @Schema(description = "상태별 데이터 (IN_PROGRESS: 현황, ENDED: 결과)")
        VoteStatusData data
) {

    public static GetVoteWidgetResponse of(VoteWidget voteWidget, List<VoteOption> voteOptions, VoteStatusData data) {
        return new GetVoteWidgetResponse(
                voteWidget.getId(),
                voteWidget.getWidgetId(),
                voteWidget.getVoteTitle().getValue(),
                voteWidget.isAnonymous(),
                voteWidget.isMultiSelectable(),
                voteWidget.getVoteStatus().name(),
                toOptionResponses(voteOptions),
                data
        );
    }

    private static List<OptionResponse> toOptionResponses(List<VoteOption> voteOptions) {
        return voteOptions.stream()
                .map(OptionResponse::from)
                .toList();
    }

    @Schema(description = "투표 선택지")
    public record OptionResponse(

            @Schema(description = "선택지 ID", example = "1")
            Long id,

            @Schema(description = "선택지 내용", example = "칼국수")
            String content
    ) {

        public static OptionResponse from(VoteOption voteOption) {
            return new OptionResponse(voteOption.getId(), voteOption.getContent().getValue());
        }
    }
}
