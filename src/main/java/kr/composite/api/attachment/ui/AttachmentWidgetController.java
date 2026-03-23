package kr.composite.api.attachment.ui;

import jakarta.validation.Valid;
import java.net.URI;
import kr.composite.api.attachment.application.AttachmentWidgetService;
import kr.composite.api.attachment.application.dto.request.AttachmentWidgetDeleteRequest;
import kr.composite.api.attachment.application.dto.request.AttachmentWidgetFindRequest;
import kr.composite.api.attachment.ui.apiSpec.AttachmentWidgetApiSpec;
import kr.composite.api.attachment.ui.dto.request.PostAttachmentWidgetRequest;
import kr.composite.api.attachment.ui.dto.response.PostAttachmentWidgetResponse;
import kr.composite.api.attachment.ui.dto.response.GetAttachmentWidgetResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AttachmentWidgetController implements AttachmentWidgetApiSpec {

    private final AttachmentWidgetService attachmentWidgetService;

    @GetMapping("/attachmentWidgets/{attachmentWidgetId}")
    public ResponseEntity<GetAttachmentWidgetResponse> readAttachmentWidget(@PathVariable Long attachmentWidgetId) {
        AttachmentWidgetFindRequest attachmentWidgetFindRequest = AttachmentWidgetFindRequest.from(attachmentWidgetId);
        GetAttachmentWidgetResponse getAttachmentWidgetResponse = GetAttachmentWidgetResponse.from(
                attachmentWidgetService.getAttachmentWidget(attachmentWidgetFindRequest));

        return ResponseEntity.ok().body(getAttachmentWidgetResponse);
    }

    @PostMapping("/attachmentWidgets")
    public ResponseEntity<PostAttachmentWidgetResponse> createAttachmentWidget(
            @Valid @RequestBody PostAttachmentWidgetRequest request
    ) {
        PostAttachmentWidgetResponse postAttachmentWidgetResponse = PostAttachmentWidgetResponse.from(
                attachmentWidgetService.addAttachmentWidget(request.toMemoWidgetAddRequest()));

        return ResponseEntity.created(URI.create("attachmentWidgets/" + postAttachmentWidgetResponse.id()))
                .body(postAttachmentWidgetResponse);
    }

    @DeleteMapping("/attachmentWidgets/{attachmentWidgetId}")
    public ResponseEntity<Void> deleteAttachmentWidget(@PathVariable("attachmentWidgetId") Long attachmentWidgetId) {
        AttachmentWidgetDeleteRequest attachmentWidgetDeleteRequest = AttachmentWidgetDeleteRequest.from(attachmentWidgetId);
        attachmentWidgetService.deleteAttachmentWidget(attachmentWidgetDeleteRequest);

        return ResponseEntity.noContent().build();
    }
}
