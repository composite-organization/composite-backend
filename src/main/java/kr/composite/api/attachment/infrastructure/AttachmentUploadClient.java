package kr.composite.api.attachment.infrastructure;

import kr.composite.api.attachment.application.dto.request.AttachmentUploadedRequest;
import org.springframework.web.multipart.MultipartFile;

public interface AttachmentUploadClient {

    AttachmentUploadedRequest uploadImage(MultipartFile file);

    String generatePresignUrl(String key);

    void deleteAttachment(String attachmentKey);
}
