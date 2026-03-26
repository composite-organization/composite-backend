package kr.composite.api.attachment.domain;

import java.util.List;
import java.util.Optional;

public interface AttachmentRepository {

    Attachment save(Attachment attachment);

    List<Attachment> findAllByAttachmentWidgetId(Long attachmentWidgetId);

    Optional<Attachment> findById(Long id);

    void deleteById(Long id);
}
