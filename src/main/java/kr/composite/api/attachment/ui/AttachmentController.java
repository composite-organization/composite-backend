package kr.composite.api.attachment.ui;

import java.net.URI;
import java.util.List;
import kr.composite.api.attachment.application.AttachmentService;
import kr.composite.api.attachment.application.dto.request.AttachmentDeleteRequest;
import kr.composite.api.attachment.application.dto.request.AttachmentFindRequest;
import kr.composite.api.attachment.application.dto.request.AttachmentWidgetFindRequest;
import kr.composite.api.attachment.ui.apiSpec.AttachmentApiSpec;
import kr.composite.api.attachment.ui.dto.response.GetAttachmentMetaDataResponse;
import kr.composite.api.attachment.ui.dto.response.GetAttachmentResponse;
import kr.composite.api.attachment.ui.dto.response.PostAttachmentResponse;
import lombok.RequiredArgsConstructor;
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

    @GetMapping("attachmentWidgets/{attachmentWidgetId}/attachments/{attachmentId}")
    public ResponseEntity<GetAttachmentResponse> readAttachment(
            @PathVariable("attachmentWidgetId") Long attachmentWidgetId,
            @PathVariable("attachmentId") Long attachmentId
    ) {
        AttachmentFindRequest attachmentFindRequest = AttachmentFindRequest.of(attachmentId, attachmentWidgetId);
        GetAttachmentResponse getAttachmentResponse = GetAttachmentResponse.from(
                attachmentService.readAttachment(attachmentFindRequest));

        return ResponseEntity.ok().body(getAttachmentResponse);
    }

    @GetMapping("attachmentWidgets/{attachmentWidgetId}/attachments")
    public ResponseEntity<List<GetAttachmentMetaDataResponse>> readAttachmentMetaData(
            @PathVariable("attachmentWidgetId") Long attachmentWidgetId
    ) {
        AttachmentWidgetFindRequest attachmentWidgetFindRequest = AttachmentWidgetFindRequest.from(attachmentWidgetId);
        List<GetAttachmentMetaDataResponse> getAttachmentMetaDataResponse = attachmentService.readAttachmentMetaData(
                        attachmentWidgetFindRequest).stream()
                .map(GetAttachmentMetaDataResponse::from)
                .toList();

        return ResponseEntity.ok().body(getAttachmentMetaDataResponse);
    }

    @PostMapping(value = "attachmentWidgets/{attachmentWidgetId}/attachments", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PostAttachmentResponse> createAttachment(
            @PathVariable("attachmentWidgetId") Long attachmentWidgetId,
            @RequestPart("attachment") MultipartFile attachment
    ) {

        AttachmentWidgetFindRequest attachmentWidgetFindRequest = AttachmentWidgetFindRequest.from(attachmentWidgetId);
        PostAttachmentResponse postAttachmentResponse = PostAttachmentResponse.from(
                attachmentService.addAttachment(attachmentWidgetFindRequest, attachment));

        return ResponseEntity.created(URI.create(
                "attachmentWidgets/" + postAttachmentResponse.attachmentWidgetId() + "/attachments/"
                        + postAttachmentResponse.id())).body(postAttachmentResponse);
    }

    @DeleteMapping("attachmentWidgets/{attachmentWidgetId}/attachments/{attachmentId}")
    public ResponseEntity<Void> deleteAttachment(
            @PathVariable("attachmentWidgetId") Long attachmentWidgetId,
            @PathVariable("attachmentId") Long attachmentId
    ) {
        AttachmentDeleteRequest attachmentDeleteRequest = AttachmentDeleteRequest.from(attachmentId,
                attachmentWidgetId);
        attachmentService.deleteAttachment(attachmentDeleteRequest);

        return ResponseEntity.noContent().build();
    }
}
