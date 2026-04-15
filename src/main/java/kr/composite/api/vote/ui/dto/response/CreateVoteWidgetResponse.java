package kr.composite.api.vote.ui.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import kr.composite.api.vote.domain.VoteOption;
import kr.composite.api.vote.domain.VoteWidget;

@Schema(description = "투표 위젯 생성 응답")
public record CreateVoteWidgetResponse(

        @Schema(description = "투표 위젯 ID", example = "1")
        Long id,

        @Schema(description = "투표 안건", example = "오늘 밥 뭐 드셨나요?")
        String title,

        @Schema(description = "익명 투표 여부", example = "false")
        boolean isAnonymous,

        @Schema(description = "복수 선택 여부", example = "false")
        boolean isMultiSelectable,

        @Schema(description = "투표 상태", example = "IN_PROGRESS")
        String status,

        @Schema(description = "투표 선택지 목록")
        List<OptionResponse> options
) {

    public static CreateVoteWidgetResponse of(VoteWidget voteWidget, List<VoteOption> voteOptions) {
        List<OptionResponse> optionResponses = voteOptions.stream()
                .map(OptionResponse::from)
                .toList();

        return new CreateVoteWidgetResponse(
                voteWidget.getId(),
                voteWidget.getVoteTitle().getValue(),
                voteWidget.isAnonymous(),
                voteWidget.isMultiSelectable(),
                voteWidget.getVoteStatus().name(),
                optionResponses
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
