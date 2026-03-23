package kr.composite.api.attachment.application;

import java.util.List;
import kr.composite.api.attachment.application.dto.request.AttachmentDeleteRequest;
import kr.composite.api.attachment.application.dto.request.AttachmentFindRequest;
import kr.composite.api.attachment.application.dto.request.AttachmentUploadedRequest;
import kr.composite.api.attachment.application.dto.request.AttachmentWidgetFindRequest;
import kr.composite.api.attachment.application.dto.response.AttachmentMetaDataResponse;
import kr.composite.api.attachment.application.dto.response.AttachmentResponse;
import kr.composite.api.attachment.application.dto.response.AttachmentUrlResponse;
import kr.composite.api.attachment.domain.Attachment;
import kr.composite.api.attachment.domain.AttachmentName;
import kr.composite.api.attachment.domain.AttachmentRepository;
import kr.composite.api.attachment.domain.AttachmentSize;
import kr.composite.api.attachment.domain.AttachmentUnit;
import kr.composite.api.attachment.infrastructure.AttachmentUploadClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class AttachmentService {

    private final AttachmentRepository attachmentRepository;
    private final AttachmentUploadClient attachmentUploadClient;

    @Transactional
    public AttachmentResponse addAttachment(AttachmentWidgetFindRequest request, MultipartFile attachment) {

        if (attachment.getSize() > 10 * 1024 * 1024) {
            throw new IllegalArgumentException("파일 크기는 10MB를 초과할 수 없습니다.");
        }

        AttachmentUploadedRequest attachmentUploadedRequest = attachmentUploadClient.uploadImage(attachment);
        AttachmentResponse attachmentResponse = addAttachmentDb(request, attachmentUploadedRequest);

        return attachmentResponse;
    }

    private AttachmentResponse addAttachmentDb(
            AttachmentWidgetFindRequest attachmentWidgetFindRequest,
            AttachmentUploadedRequest attachmentUploadedRequest
    ) {
        AttachmentName attachmentName = new AttachmentName(attachmentUploadedRequest.name());
        AttachmentSize attachmentSize = new AttachmentSize(attachmentUploadedRequest.size());
        AttachmentUnit attachmentUnit = attachmentSize.getAppropriateUnit();

        Attachment attachment = new Attachment(
                attachmentWidgetFindRequest.id(),
                attachmentUploadedRequest.key(),
                attachmentName,
                attachmentSize,
                attachmentUnit);

        attachmentRepository.save(attachment);

        return AttachmentResponse.from(attachment);
    }

    public List<AttachmentMetaDataResponse> readAttachmentMetaData(AttachmentWidgetFindRequest request) {
        List<Attachment> attachments = attachmentRepository.findAllByAttachmentWidgetId(request.id());

        return attachments.stream()
                .map(AttachmentMetaDataResponse::from)
                .toList();
    }

    public AttachmentUrlResponse readAttachment(AttachmentFindRequest request) {
        Attachment attachment = attachmentRepository.findById(request.attachmentId())
                .orElseThrow(() -> new IllegalArgumentException());

        String presignedUrl = attachmentUploadClient.generatePresignUrl(attachment.getAttachmentKey());

        return AttachmentUrlResponse.from(presignedUrl);
    }

    @Transactional
    public void deleteAttachment(AttachmentDeleteRequest attachmentDeleteRequest) {
        Attachment attachment = attachmentRepository.findById(attachmentDeleteRequest.attachmentId())
                .orElse(null);

        if (attachment == null) {
            return;
        }

        attachmentRepository.deleteById(attachmentDeleteRequest.attachmentId());
        attachmentUploadClient.deleteAttachment(attachment.getAttachmentKey());
    }
}
