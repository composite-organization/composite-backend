package kr.composite.api.attachment.ui.apiSpec;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.composite.api.attachment.ui.dto.request.PostAttachmentWidgetRequest;
import kr.composite.api.attachment.ui.dto.response.GetAttachmentWidgetResponse;
import kr.composite.api.attachment.ui.dto.response.PostAttachmentWidgetResponse;
import kr.composite.api.user.domain.User;
import kr.composite.api.user.ui.requestuser.RequestUser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "수업자료 위젯 API", description = "수업자료 위젯 관련 API 명세입니다.")
public interface AttachmentWidgetApiSpec {

    @Operation(summary = "수업자료 위젯 조회", description = "위젯 ID로 수업자료 위젯을 조회합니다.")
    ResponseEntity<GetAttachmentWidgetResponse> readAttachmentWidget(
            @Parameter(hidden = true) @RequestUser User user,
            @Parameter(description = "수업자료 위젯 ID", example = "1")
            @PathVariable Long attachmentWidgetId
    );

    @Operation(summary = "수업자료 위젯 생성", description = "새로운 수업자료 위젯을 생성합니다.")
    ResponseEntity<PostAttachmentWidgetResponse> createAttachmentWidget(
            @Parameter(hidden = true) @RequestUser User user,
            @RequestBody PostAttachmentWidgetRequest request
    );

    @Operation(summary = "수업자료 위젯 삭제", description = "위젯 ID로 수업자료 위젯을 삭제합니다.")
    ResponseEntity<Void> deleteAttachmentWidget(
            @Parameter(hidden = true) @RequestUser User user,
            @Parameter(description = "수업자료 위젯 ID", example = "1")
            @PathVariable Long attachmentWidgetId
    );
}
