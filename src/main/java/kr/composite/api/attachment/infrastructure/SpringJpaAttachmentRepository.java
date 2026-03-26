package kr.composite.api.attachment.infrastructure;

import java.util.List;
import kr.composite.api.attachment.domain.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringJpaAttachmentRepository extends JpaRepository<Attachment, Long> {

    List<Attachment> findAllByAttachmentWidgetId(Long attachmentWidgetId);
}
