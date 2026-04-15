package kr.composite.api.vote.ui.apiSpec;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.composite.api.user.domain.User;
import kr.composite.api.vote.ui.dto.request.PostVoteSubmissionRequest;
import kr.composite.api.vote.ui.dto.request.PostVoteWidgetRequest;
import kr.composite.api.vote.ui.dto.request.UpdateVoteStatusRequest;
import kr.composite.api.vote.ui.dto.response.CreateVoteWidgetResponse;
import kr.composite.api.vote.ui.dto.response.GetVoteWidgetResponse;
import org.springframework.http.ResponseEntity;

@Tag(name = "투표 위젯 API", description = "투표 위젯 관련 API 명세입니다.")
public interface VoteWidgetApiSpec {

    @Operation(summary = "투표 위젯 생성", description = "새로운 투표 위젯을 생성합니다.")
    ResponseEntity<CreateVoteWidgetResponse> createVoteWidget(
            @Parameter(hidden = true)
            User user,
            PostVoteWidgetRequest request
    );

    @Operation(
            summary = "투표 위젯 조회",
            description = "투표 위젯을 조회합니다. "
                    + "participationResponse(선택지별 현황)는 상태와 무관하게 항상 포함됩니다. "
                    + "ENDED 상태인 경우 endedResponse(선정 결과)가 추가로 포함됩니다. "
                    + "폴링으로 실시간 현황 및 결과를 확인할 수 있습니다."
    )
    ResponseEntity<GetVoteWidgetResponse> readVoteWidget(
            @Parameter(hidden = true)
            User user,
            @Parameter(description = "투표 위젯 ID", example = "1")
            Long voteWidgetId
    );

    @Operation(summary = "투표 상태 변경", description = "투표의 상태를 변경합니다.")
    ResponseEntity<Void> updateVoteStatus(
            @Parameter(hidden = true)
            User user,
            @Parameter(description = "투표 위젯 ID", example = "1")
            Long voteWidgetId,
            UpdateVoteStatusRequest request
    );

    @Operation(summary = "투표 제출", description = "학생이 투표를 제출합니다.")
    ResponseEntity<Void> createVoteSubmission(
            @Parameter(hidden = true)
            User user,
            @Parameter(description = "투표 위젯 ID", example = "1")
            Long voteWidgetId,
            PostVoteSubmissionRequest request
    );

    @Operation(summary = "투표 위젯 삭제", description = "투표 위젯을 삭제합니다.")
    ResponseEntity<Void> deleteVoteWidget(
            @Parameter(hidden = true)
            User user,
            @Parameter(description = "투표 위젯 ID", example = "1")
            Long voteWidgetId
    );
}
