package kr.composite.api.attachment.domain;

import java.util.Optional;

public interface AttachmentWidgetRepository {

    Optional<AttachmentWidget> findById(Long id);

    Optional<AttachmentWidget> findByWidgetId(Long widgetId);

    AttachmentWidget save(AttachmentWidget attachmentWidget);

    void deleteById(Long id);
}
