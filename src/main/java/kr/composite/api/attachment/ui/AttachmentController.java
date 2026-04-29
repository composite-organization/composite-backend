package kr.composite.api.attachment.ui;

import java.util.List;
import kr.composite.api.attachment.application.AttachmentService;
import kr.composite.api.attachment.application.dto.request.AttachmentDeleteRequest;
import kr.composite.api.attachment.application.dto.request.AttachmentFindRequest;
import kr.composite.api.attachment.application.dto.request.AttachmentWidgetFindRequest;
import kr.composite.api.attachment.ui.apiSpec.AttachmentApiSpec;
import kr.composite.api.attachment.ui.dto.request.FileUploadRequest;
import kr.composite.api.attachment.ui.dto.response.GetAttachmentMetaDataResponse;
import kr.composite.api.attachment.ui.dto.response.GetAttachmentResponse;
import kr.composite.api.attachment.ui.dto.response.PostAttachmentResponse;
import kr.composite.api.user.domain.User;
import kr.composite.api.user.ui.requestuser.RequestUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class AttachmentController implements AttachmentApiSpec {

    private final AttachmentService attachmentService;

    @Override
    @GetMapping("attachmentWidgets/{attachmentWidgetId}/attachments/{attachmentId}")
    public ResponseEntity<GetAttachmentResponse> readAttachment(
            @RequestUser User user,
            @PathVariable("attachmentWidgetId") Long attachmentWidgetId,
            @PathVariable("attachmentId") Long attachmentId
    ) {
        AttachmentFindRequest attachmentFindRequest = AttachmentFindRequest.of(attachmentId, attachmentWidgetId);
        GetAttachmentResponse getAttachmentResponse = GetAttachmentResponse.from(
                attachmentService.getAttachment(user, attachmentFindRequest));

        return ResponseEntity.ok().body(getAttachmentResponse);
    }

    @Override
    @GetMapping("attachmentWidgets/{attachmentWidgetId}/attachments")
    public ResponseEntity<List<GetAttachmentMetaDataResponse>> readAttachmentMetaData(
            @RequestUser User user,
            @PathVariable("attachmentWidgetId") Long attachmentWidgetId
    ) {
        AttachmentWidgetFindRequest attachmentWidgetFindRequest = AttachmentWidgetFindRequest.from(attachmentWidgetId);
        List<GetAttachmentMetaDataResponse> getAttachmentMetaDataResponse =
                attachmentService.getAttachmentMetaData(user, attachmentWidgetFindRequest).stream()
                        .map(GetAttachmentMetaDataResponse::from)
                        .toList();

        return ResponseEntity.ok().body(getAttachmentMetaDataResponse);
    }

    @Override
    @PostMapping(value = "attachmentWidgets/{attachmentWidgetId}/attachments", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PostAttachmentResponse> createAttachment(
            @RequestUser User user,
            @PathVariable("attachmentWidgetId") Long attachmentWidgetId,
            @RequestPart("attachment") MultipartFile attachment
    ) {
        AttachmentWidgetFindRequest attachmentWidgetFindRequest = AttachmentWidgetFindRequest.from(attachmentWidgetId);
        FileUploadRequest fileUploadRequest = FileUploadRequest.from(attachment);

        PostAttachmentResponse postAttachmentResponse = PostAttachmentResponse.from(
                attachmentService.addAttachment(user, attachmentWidgetFindRequest, fileUploadRequest));

        return ResponseEntity.status(HttpStatus.CREATED).body(postAttachmentResponse);
    }

    @Override
    @DeleteMapping("attachmentWidgets/{attachmentWidgetId}/attachments/{attachmentId}")
    public ResponseEntity<Void> deleteAttachment(
            @RequestUser User user,
            @PathVariable("attachmentWidgetId") Long attachmentWidgetId,
            @PathVariable("attachmentId") Long attachmentId
    ) {
        AttachmentDeleteRequest attachmentDeleteRequest =
                AttachmentDeleteRequest.of(attachmentId, attachmentWidgetId);
        attachmentService.deleteAttachment(user, attachmentDeleteRequest);

        return ResponseEntity.noContent().build();
    }
}
