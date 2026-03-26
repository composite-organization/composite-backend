package kr.composite.api.attachment.domain;

import java.io.InputStream;

public interface AttachmentStorage {

    void upload(InputStream inputStream, String key, String contentType, Long size);

    void deleteAttachment(Attachment attachment);
}
