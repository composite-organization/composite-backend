package kr.composite.api.attachment.infrastructure;

import java.util.List;
import java.util.Optional;
import kr.composite.api.attachment.domain.Attachment;
import kr.composite.api.attachment.domain.AttachmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JpaAttachmentRepository implements AttachmentRepository {

    private final SpringJpaAttachmentRepository springJpaAttachmentRepository;

    @Override
    public Attachment save(Attachment attachment) {
        return springJpaAttachmentRepository.save(attachment);
    }

    @Override
    public List<Attachment> findAllByAttachmentWidgetId(Long attachmentWidgetId) {
        return springJpaAttachmentRepository.findAllByAttachmentWidgetId(attachmentWidgetId);
    }

    @Override
    public Optional<Attachment> findById(Long id) {
        return springJpaAttachmentRepository.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        springJpaAttachmentRepository.deleteById(id);
    }
}
