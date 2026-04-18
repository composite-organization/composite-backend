package kr.composite.api.attachment.ui.apiSpec;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import kr.composite.api.attachment.ui.dto.response.GetAttachmentMetaDataResponse;
import kr.composite.api.attachment.ui.dto.response.GetAttachmentResponse;
import kr.composite.api.attachment.ui.dto.response.PostAttachmentResponse;
import kr.composite.api.user.domain.User;
import kr.composite.api.user.ui.requestuser.RequestUser;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "수업자료 API", description = "수업자료 업로드 및 관리를 위한 API 명세입니다.")
public interface AttachmentApiSpec {

    @Operation(summary = "수업자료 상세 조회", description = "특정 수업자료의 상세 정보(다운로드 URL 등)를 조회합니다.")
    ResponseEntity<GetAttachmentResponse> readAttachment(
            @Parameter(hidden = true) @RequestUser User user,
            @Parameter(description = "수업자료 위젯 ID", example = "1")
            @PathVariable("attachmentWidgetId") Long attachmentWidgetId,
            @Parameter(description = "수업자료 ID", example = "1")
            @PathVariable("attachmentId") Long attachmentId
    );

    @Operation(summary = "수업자료 목록 조회", description = "위젯에 속한 모든 수업자료의 메타데이터 목록을 조회합니다.")
    ResponseEntity<List<GetAttachmentMetaDataResponse>> readAttachmentMetaData(
            @Parameter(hidden = true) @RequestUser User user,
            @Parameter(description = "수업자료 위젯 ID", example = "1")
            @PathVariable("attachmentWidgetId") Long attachmentWidgetId
    );

    @Operation(
            summary = "수업자료 업로드",
            description = "파일을 S3에 업로드하고 메타데이터를 저장합니다.",
            requestBody = @RequestBody(
                    content = @Content(
                            mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                            schema = @Schema(type = "object"),
                            // Swagger UI에서 파일 선택 버튼이 나오게 설정
                            schemaProperties = {
                                    @io.swagger.v3.oas.annotations.media.SchemaProperty(
                                            name = "attachment",
                                            schema = @Schema(type = "string", format = "binary", description = "업로드할 파일")
                                    )
                            }
                    )
            )
    )
    ResponseEntity<PostAttachmentResponse> createAttachment(
            @Parameter(hidden = true) @RequestUser User user,
            @Parameter(description = "수업자료 위젯 ID", example = "1")
            @PathVariable("attachmentWidgetId") Long attachmentWidgetId,
            @RequestPart("attachment") MultipartFile attachment
    );

    @Operation(summary = "수업자료 삭제", description = "특정 수업자료을 S3와 DB에서 삭제합니다.")
    ResponseEntity<Void> deleteAttachment(
            @Parameter(hidden = true) @RequestUser User user,
            @Parameter(description = "수업자료 위젯 ID", example = "1")
            @PathVariable("attachmentWidgetId") Long attachmentWidgetId,
            @Parameter(description = "수업자료 ID", example = "1")
            @PathVariable("attachmentId") Long attachmentId
    );
}
