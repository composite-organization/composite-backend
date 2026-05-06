package kr.composite.api.attachment.domain;

import java.util.List;
import java.util.Optional;

public interface AttachmentWidgetRepository {

    Optional<AttachmentWidget> findById(Long id);

    List<AttachmentWidget> findAllByWidgetIdIn(List<Long> widgetIds);

    AttachmentWidget save(AttachmentWidget attachmentWidget);

    void deleteById(Long id);
}
