package kr.composite.api.attachment.infrastructure;

import java.util.List;
import java.util.Optional;
import kr.composite.api.attachment.domain.AttachmentWidget;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringJpaAttachmentWidgetRepository extends JpaRepository<AttachmentWidget, Long> {

    List<AttachmentWidget> findAllByWidgetIdIn(List<Long> widgetIds);
}
