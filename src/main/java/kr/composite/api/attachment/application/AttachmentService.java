package kr.composite.api.attachment.application;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import kr.composite.api.attachment.application.dto.request.AttachmentDeleteRequest;
import kr.composite.api.attachment.application.dto.request.AttachmentFindRequest;
import kr.composite.api.attachment.application.dto.request.AttachmentUploadedRequest;
import kr.composite.api.attachment.application.dto.request.AttachmentWidgetFindRequest;
import kr.composite.api.attachment.application.dto.response.AttachmentMetaDataResponse;
import kr.composite.api.attachment.application.dto.response.AttachmentResponse;
import kr.composite.api.attachment.application.dto.response.AttachmentUriResponse;
import kr.composite.api.attachment.domain.Attachment;
import kr.composite.api.attachment.domain.AttachmentName;
import kr.composite.api.attachment.domain.AttachmentRepository;
import kr.composite.api.attachment.domain.AttachmentSize;
import kr.composite.api.attachment.domain.AttachmentStorage;
import kr.composite.api.attachment.domain.AttachmentUnit;
import kr.composite.api.attachment.domain.AttachmentUriProvider;
import kr.composite.api.attachment.ui.dto.request.FileUploadRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class AttachmentService {

    private final AttachmentRepository attachmentRepository;
    private final AttachmentStorage attachmentStorage;
    private final AttachmentUriProvider attachmentUriProvider;

    @Transactional
    public AttachmentResponse addAttachment(AttachmentWidgetFindRequest request, FileUploadRequest file) {

        validateFileSize(file);

        String originalFilename = file.originalFileName();
        String contentType = file.contentType();
        Long size = file.size();

        String key = createKey(originalFilename);

        try (InputStream inputStream = file.inputStream()) {
            attachmentStorage.upload(inputStream, key, contentType, size);
        } catch (IOException e) {
            throw AttachmentApplicationException.fileReadFailed();
        }

        AttachmentUploadedRequest attachmentUploadedRequest = new AttachmentUploadedRequest(
                originalFilename,
                size,
                key
        );

        AttachmentResponse attachmentResponse = addAttachmentDb(request, attachmentUploadedRequest);

        return attachmentResponse;
    }

    private void validateFileSize(FileUploadRequest file) {
        long maxSizeBytes = 10 * AttachmentUnit.MB.getByteSize(); // 10MB
        if (file.size() > maxSizeBytes) {
            throw AttachmentApplicationException.fileSizeExceeded(file.size(), maxSizeBytes);
        }
    }

    private String createKey(String originalFilename) {
        String extension =
                Optional.ofNullable(StringUtils.getFilenameExtension(originalFilename))
                        .filter(StringUtils::hasText)
                        .orElse("");

        String key = UUID.randomUUID() + "." + extension;

        return key;
    }

    private AttachmentResponse addAttachmentDb(
            AttachmentWidgetFindRequest attachmentWidgetFindRequest,
            AttachmentUploadedRequest attachmentUploadedRequest
    ) {
        AttachmentName attachmentName = new AttachmentName(attachmentUploadedRequest.name());
        AttachmentSize attachmentSize = new AttachmentSize(attachmentUploadedRequest.size());
        AttachmentUnit attachmentUnit = AttachmentUnit.getAppropriateUnit(attachmentSize.getValue());

        Attachment attachment = new Attachment(
                attachmentWidgetFindRequest.id(),
                attachmentUploadedRequest.key(),
                attachmentName,
                attachmentSize,
                attachmentUnit
        );

        attachmentRepository.save(attachment);

        return AttachmentResponse.from(attachment);
    }

    @Transactional(readOnly = true)
    public List<AttachmentMetaDataResponse> readAttachmentMetaData(AttachmentWidgetFindRequest request) {
        List<Attachment> attachments = attachmentRepository.findAllByAttachmentWidgetId(request.id());

        return attachments.stream()
                .map(AttachmentMetaDataResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public AttachmentUriResponse readAttachment(AttachmentFindRequest request) {
        Attachment attachment = attachmentRepository.findById(request.attachmentId())
                .orElseThrow(AttachmentApplicationException::attachmentNotFound);

        String uri = attachmentUriProvider.getUri(attachment);

        return AttachmentUriResponse.from(uri);
    }

    @Transactional
    public void deleteAttachment(AttachmentDeleteRequest attachmentDeleteRequest) {
        Attachment attachment = attachmentRepository.findById(attachmentDeleteRequest.attachmentId())
                .orElse(null);

        if (attachment == null) {
            return;
        }

        attachmentRepository.deleteById(attachmentDeleteRequest.attachmentId());
        attachmentStorage.deleteAttachment(attachment);
    }
}
