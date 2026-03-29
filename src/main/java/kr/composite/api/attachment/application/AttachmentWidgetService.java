package kr.composite.api.attachment.application;

import kr.composite.api.attachment.application.dto.request.AttachmentWidgetAddRequest;
import kr.composite.api.attachment.application.dto.request.AttachmentWidgetDeleteRequest;
import kr.composite.api.attachment.application.dto.request.AttachmentWidgetFindRequest;
import kr.composite.api.attachment.application.dto.response.AttachmentWidgetResponse;
import kr.composite.api.attachment.domain.AttachmentWidget;
import kr.composite.api.attachment.domain.AttachmentWidgetRepository;
import kr.composite.api.widget.domain.Widget;
import kr.composite.api.widget.domain.WidgetRepository;
import kr.composite.api.widget.domain.WidgetType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AttachmentWidgetService {

    private final AttachmentWidgetRepository attachmentWidgetRepository;
    private final WidgetRepository widgetRepository;

    @Transactional(readOnly = true)
    public AttachmentWidgetResponse getAttachmentWidget(AttachmentWidgetFindRequest request) {
        AttachmentWidget attachmentWidget = attachmentWidgetRepository.findById(request.id())
                .orElseThrow(AttachmentApplicationException::widgetNotFound);
        AttachmentWidgetResponse attachmentWidgetResponse = AttachmentWidgetResponse.from(attachmentWidget);

        return attachmentWidgetResponse;
    }

    @Transactional
    public AttachmentWidgetResponse addAttachmentWidget(AttachmentWidgetAddRequest request) {
        Widget widget = new Widget(request.lessonId(), WidgetType.ATTACHMENT);
        widgetRepository.save(widget);

        AttachmentWidget attachmentWidget = new AttachmentWidget(widget.getId());
        attachmentWidgetRepository.save(attachmentWidget);

        AttachmentWidgetResponse attachmentWidgetResponse = AttachmentWidgetResponse.from(attachmentWidget);

        return attachmentWidgetResponse;
    }

    // TODO: 자료공유 위젯 삭제 시 자료 삭제 여부
    @Transactional
    public void deleteAttachmentWidget(AttachmentWidgetDeleteRequest request) {
        AttachmentWidget attachmentWidget = attachmentWidgetRepository.findById(request.id()).orElse(null);
        if (attachmentWidget == null) {
            return;
        }

        widgetRepository.deleteById(attachmentWidget.getWidgetId());
        attachmentWidgetRepository.deleteById(attachmentWidget.getId());
    }
}
