package kr.composite.api.attachment.infrastructure;

import kr.composite.api.attachment.domain.AttachmentWidget;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringJpaAttachmentWidgetRepository extends JpaRepository<AttachmentWidget, Long> {
}
