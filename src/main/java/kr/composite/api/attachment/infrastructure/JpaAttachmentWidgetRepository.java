package kr.composite.api.attachment.infrastructure;

import java.util.List;
import java.util.Optional;
import kr.composite.api.attachment.domain.AttachmentWidget;
import kr.composite.api.attachment.domain.AttachmentWidgetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JpaAttachmentWidgetRepository implements AttachmentWidgetRepository {

    private final SpringJpaAttachmentWidgetRepository springJpaAttachmentWidgetRepository;

    @Override
    public Optional<AttachmentWidget> findById(Long id) {
        return springJpaAttachmentWidgetRepository.findById(id);
    }

    @Override
    public List<AttachmentWidget> findAllByWidgetIdIn(List<Long> widgetIds) {
        return springJpaAttachmentWidgetRepository.findAllByWidgetIdIn(widgetIds);
    }

    @Override
    public AttachmentWidget save(AttachmentWidget attachmentWidget) {
        return springJpaAttachmentWidgetRepository.save(attachmentWidget);
    }

    @Override
    public void deleteById(Long id) {
        springJpaAttachmentWidgetRepository.deleteById(id);
    }
}
