package kr.composite.api.vote.ui.apiSpec;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
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
            PostVoteWidgetRequest request
    );

    @Operation(
            summary = "투표 위젯 조회",
            description = "투표 위젯을 조회합니다. 상태에 따라 응답이 달라집니다. "
                    + "IN_PROGRESS: 현황(data) 포함, ENDED: 결과(data) 포함. "
                    + "폴링으로 실시간 현황/결과를 확인할 수 있습니다."
    )
    ResponseEntity<GetVoteWidgetResponse> readVoteWidget(
            @Parameter(description = "투표 위젯 ID", example = "1")
            Long voteWidgetId
    );

    @Operation(summary = "투표 상태 변경", description = "투표의 상태를 변경합니다.")
    ResponseEntity<Void> updateVoteStatus(
            @Parameter(description = "투표 위젯 ID", example = "1")
            Long voteWidgetId,
            UpdateVoteStatusRequest request
    );

    @Operation(summary = "투표 제출", description = "학생이 투표를 제출합니다.")
    ResponseEntity<Void> createVoteSubmission(
            @Parameter(description = "투표 위젯 ID", example = "1")
            Long voteWidgetId,
            PostVoteSubmissionRequest request
    );

    @Operation(summary = "투표 위젯 삭제", description = "투표 위젯을 삭제합니다.")
    ResponseEntity<Void> deleteVoteWidget(
            @Parameter(description = "투표 위젯 ID", example = "1")
            Long voteWidgetId
    );
}
