package kr.composite.api.attachment.ui;

import jakarta.validation.Valid;
import kr.composite.api.attachment.application.AttachmentWidgetService;
import kr.composite.api.attachment.application.dto.request.AttachmentWidgetDeleteRequest;
import kr.composite.api.attachment.application.dto.request.AttachmentWidgetFindRequest;
import kr.composite.api.attachment.ui.apiSpec.AttachmentWidgetApiSpec;
import kr.composite.api.attachment.ui.dto.request.PostAttachmentWidgetRequest;
import kr.composite.api.attachment.ui.dto.response.GetAttachmentWidgetResponse;
import kr.composite.api.attachment.ui.dto.response.PostAttachmentWidgetResponse;
import kr.composite.api.user.domain.User;
import kr.composite.api.user.ui.requestuser.RequestUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

    @Override
    @GetMapping("/attachmentWidgets/{attachmentWidgetId}")
    public ResponseEntity<GetAttachmentWidgetResponse> readAttachmentWidget(
            @RequestUser User user,
            @PathVariable("attachmentWidgetId") Long attachmentWidgetId
    ) {
        AttachmentWidgetFindRequest attachmentWidgetFindRequest = AttachmentWidgetFindRequest.from(attachmentWidgetId);
        GetAttachmentWidgetResponse getAttachmentWidgetResponse = GetAttachmentWidgetResponse.from(
                attachmentWidgetService.getAttachmentWidget(user, attachmentWidgetFindRequest));

        return ResponseEntity.ok().body(getAttachmentWidgetResponse);
    }

    @Override
    @PostMapping("/attachmentWidgets")
    public ResponseEntity<PostAttachmentWidgetResponse> createAttachmentWidget(
            @RequestUser User user,
            @Valid @RequestBody PostAttachmentWidgetRequest request
    ) {
        PostAttachmentWidgetResponse postAttachmentWidgetResponse = PostAttachmentWidgetResponse.from(
                attachmentWidgetService.addAttachmentWidget(user, request.toMemoWidgetAddRequest()));

        return ResponseEntity.status(HttpStatus.CREATED).body(postAttachmentWidgetResponse);
    }

    @Override
    @DeleteMapping("/attachmentWidgets/{attachmentWidgetId}")
    public ResponseEntity<Void> deleteAttachmentWidget(
            @RequestUser User user,
            @PathVariable("attachmentWidgetId") Long attachmentWidgetId
    ) {
        AttachmentWidgetDeleteRequest attachmentWidgetDeleteRequest = AttachmentWidgetDeleteRequest.from(
                attachmentWidgetId);
        attachmentWidgetService.deleteAttachmentWidget(user, attachmentWidgetDeleteRequest);

        return ResponseEntity.noContent().build();
    }
}
