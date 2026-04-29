package kr.composite.api.memo.ui.apiSpec;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.composite.api.memo.ui.dto.request.PostMemoWidgetRequest;
import kr.composite.api.memo.ui.dto.request.UpdateMemoWidgetRequest;
import kr.composite.api.memo.ui.dto.response.CreateMemoWidgetResponse;
import kr.composite.api.memo.ui.dto.response.GetMemoWidgetResponse;
import kr.composite.api.memo.ui.dto.response.UpdateMemoWidgetResponse;
import kr.composite.api.user.domain.User;
import kr.composite.api.user.ui.requestuser.RequestUser;
import org.springframework.http.ResponseEntity;

@Tag(name = "메모 위젯 API", description = "메모 위젯 관련 API 명세입니다.")
public interface MemoWidgetApiSpec {

    @Operation(summary = "메모 위젯 조회", description = "메모 위젯 ID로 메모를 조회합니다.")
    ResponseEntity<GetMemoWidgetResponse> readMemoWidget(
            @Parameter(hidden = true) @RequestUser User user,
            @Parameter(description = "메모 위젯 ID", example = "1")
            Long memoWidgetId
    );

    @Operation(summary = "메모 위젯 생성", description = "새로운 메모 위젯을 생성합니다.")
    ResponseEntity<CreateMemoWidgetResponse> createMemoWidget(
            @Parameter(hidden = true) @RequestUser User user,
            PostMemoWidgetRequest request
    );

    @Operation(summary = "메모 위젯 수정", description = "기존 메모 위젯을 수정합니다.")
    ResponseEntity<UpdateMemoWidgetResponse> updateMemoWidget(
            @Parameter(hidden = true) @RequestUser User user,
            @Parameter(description = "메모 위젯 ID", example = "1")
            Long memoWidgetId,
            UpdateMemoWidgetRequest request
    );

    @Operation(summary = "메모 위젯 삭제", description = "메모 위젯 ID로 메모를 삭제합니다.")
    ResponseEntity<Void> deleteMemoWidget(
            @Parameter(hidden = true) @RequestUser User user,
            @Parameter(description = "메모 위젯 ID", example = "1")
            Long memoWidgetId
    );
}

