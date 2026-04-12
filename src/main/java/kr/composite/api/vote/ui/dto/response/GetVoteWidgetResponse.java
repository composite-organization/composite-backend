package kr.composite.api.vote.ui.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import kr.composite.api.vote.domain.VoteOption;
import kr.composite.api.vote.domain.VoteStatus;
import kr.composite.api.vote.domain.VoteSubmissions;
import kr.composite.api.vote.domain.VoteWidget;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "투표 위젯 조회 응답. 투표 진행 중이면 participationData만 포함, 종료 시 endedData도 함께 포함됩니다.")
public record GetVoteWidgetResponse(

        @Schema(description = "투표 위젯 ID", example = "1")
        Long id,

        @Schema(description = "투표 안건", example = "오늘 밥 뭐 드셨나요?")
        String title,

        @Schema(description = "익명 투표 여부", example = "false")
        boolean isAnonymous,

        @Schema(description = "복수 선택 여부", example = "false")
        boolean isMultiSelectable,

        @Schema(description = "현재 투표 상태 (IN_PROGRESS, ENDED)", example = "IN_PROGRESS")
        String status,

        @Schema(description = "투표 선택지 목록")
        List<OptionResponse> options,

        @Schema(description = "선택지별 투표 현황 (항상 포함)")
        VoteParticipationResponse participationResponse,

        @Schema(description = "투표 종료 결과 (ENDED 상태인 경우에만 포함)")
        VoteEndedResponse endedResponse
) {

    public static GetVoteWidgetResponse of(
            VoteWidget voteWidget,
            VoteSubmissions voteSubmissions,
            VoteParticipationResponse participationResponse
    ) {
        return new GetVoteWidgetResponse(
                voteWidget.getId(),
                voteWidget.getVoteTitle().getValue(),
                voteWidget.isAnonymous(),
                voteWidget.isMultiSelectable(),
                voteWidget.getVoteStatus().name(),
                voteSubmissions.getVoteOptions().stream()
                        .map(OptionResponse::from)
                        .toList(),
                participationResponse,
                voteWidget.getVoteStatus() == VoteStatus.IN_PROGRESS
                        ? null
                        : VoteEndedResponse.from(voteSubmissions)
        );
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
