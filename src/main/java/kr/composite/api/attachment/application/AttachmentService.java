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
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class AttachmentService {

    private final AttachmentRepository attachmentRepository;
    private final AttachmentStorage attachmentStorage;
    private final AttachmentUriProvider attachmentUriProvider;

    @Value("${external.aws.s3.attachment.key.prefix}")
    private String keyPrefix;

    @Transactional
    public AttachmentResponse addAttachment(AttachmentWidgetFindRequest request, MultipartFile attachment) {

        Long maxSizeBytes = 10 * AttachmentUnit.MB.getThreshold();
        if (attachment.getSize() > maxSizeBytes) {
            throw new IllegalArgumentException("파일 크기는 10MB를 초과할 수 없습니다.");
        }

        String originalFilename = attachment.getOriginalFilename();
        String contentType = attachment.getContentType();
        Long size = attachment.getSize();

        String key = createKey(originalFilename);

        try (InputStream inputStream = attachment.getInputStream()) {
            attachmentStorage.upload(inputStream, key, contentType, size);
        } catch (IOException e) {
            throw new IllegalArgumentException("파일 읽기 중 오류가 발생했습니다.", e);
        }

        AttachmentUploadedRequest attachmentUploadedRequest = new AttachmentUploadedRequest(
                originalFilename,
                size,
                key
        );

        AttachmentResponse attachmentResponse = addAttachmentDb(request, attachmentUploadedRequest);

        return attachmentResponse;
    }

    private String createKey(String originalFilename) {
        String extension =
                Optional.ofNullable(StringUtils.getFilenameExtension(originalFilename))
                        .filter(StringUtils::hasText)
                        .orElse("");

        String key = keyPrefix + UUID.randomUUID() + "." + extension;

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

    public List<AttachmentMetaDataResponse> readAttachmentMetaData(AttachmentWidgetFindRequest request) {
        List<Attachment> attachments = attachmentRepository.findAllByAttachmentWidgetId(request.id());

        return attachments.stream()
                .map(AttachmentMetaDataResponse::from)
                .toList();
    }

    public AttachmentUriResponse readAttachment(AttachmentFindRequest request) {
        Attachment attachment = attachmentRepository.findById(request.attachmentId())
                .orElseThrow(() -> new IllegalArgumentException());

        String presignedUrl = attachmentUriProvider.getReadUri(attachment.getAttachmentKey());

        return AttachmentUriResponse.from(presignedUrl);
    }

    @Transactional
    public void deleteAttachment(AttachmentDeleteRequest attachmentDeleteRequest) {
        Attachment attachment = attachmentRepository.findById(attachmentDeleteRequest.attachmentId())
                .orElse(null);

        if (attachment == null) {
            return;
        }

        attachmentRepository.deleteById(attachmentDeleteRequest.attachmentId());
        attachmentStorage.deleteAttachment(attachment.getAttachmentKey());
    }
}
