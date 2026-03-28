package kr.composite.api.attachment.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import jakarta.persistence.EntityManager;
import kr.composite.api.attachment.application.dto.request.AttachmentWidgetAddRequest;
import kr.composite.api.attachment.application.dto.request.AttachmentWidgetDeleteRequest;
import kr.composite.api.attachment.application.dto.request.AttachmentWidgetFindRequest;
import kr.composite.api.attachment.application.dto.response.AttachmentWidgetResponse;
import kr.composite.api.attachment.domain.AttachmentWidget;
import kr.composite.api.attachment.domain.AttachmentWidgetRepository;
import kr.composite.api.widget.domain.Widget;
import kr.composite.api.widget.domain.WidgetRepository;
import kr.composite.api.widget.domain.WidgetType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class AttachmentWidgetServiceTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private AttachmentWidgetRepository attachmentWidgetRepository;

    @Autowired
    private WidgetRepository widgetRepository;

    @Autowired
    private AttachmentWidgetService attachmentWidgetService;

    @Test
    @DisplayName("자료 공유 위젯을 조회할 수 있다")
    void 자료_공유_위젯을_조회할_수_있다() {
        // given
        Widget widget = new Widget(1L, WidgetType.ATTACHMENT);
        widgetRepository.save(widget);

        AttachmentWidget attachmentWidget = new AttachmentWidget(widget.getId());
        attachmentWidgetRepository.save(attachmentWidget);

        entityManager.flush();
        entityManager.clear();

        AttachmentWidgetFindRequest request = AttachmentWidgetFindRequest.from(attachmentWidget.getId());

        // when
        AttachmentWidgetResponse response = attachmentWidgetService.getAttachmentWidget(request);

        // then
        assertThat(response.id()).isEqualTo(attachmentWidget.getId());
        assertThat(response.widgetId()).isEqualTo(widget.getId());
    }

    @Test
    @DisplayName("존재하지 않는 자료 공유 위젯 조회 시 예외가 발생한다")
    void 존재하지_않는_자료_공유_위젯_조회_예외_테스트() {
        // given
        AttachmentWidgetFindRequest request = AttachmentWidgetFindRequest.from(9999L);

        // when & then
        assertThatThrownBy(() -> attachmentWidgetService.getAttachmentWidget(request))
                .isInstanceOf(AttachmentApplicationException.class);
    }

    @Test
    @DisplayName("자료 공유 위젯을 생성할 수 있다")
    void 자료_공유_위젯을_생성할_수_있다() {
        // given
        Long lessonId = 1L;
        AttachmentWidgetAddRequest request = new AttachmentWidgetAddRequest(lessonId);

        // when
        AttachmentWidgetResponse response = attachmentWidgetService.addAttachmentWidget(request);

        // then
        AttachmentWidget savedWidget = attachmentWidgetRepository.findById(response.id()).orElse(null);

        assertThat(savedWidget).isNotNull();
        assertThat(savedWidget.getWidgetId()).isNotNull();

        Widget widget = widgetRepository.findById(savedWidget.getWidgetId()).orElse(null);
        assertThat(widget).isNotNull();
        assertThat(widget.getWidgetType()).isEqualTo(WidgetType.ATTACHMENT);
    }

    @Test
    @DisplayName("자료 공유 위젯을 삭제하면 연결된 Widget도 함께 삭제된다")
    void 자료_공유_위젯을_삭제할_수_있다() {
        // given
        Widget widget = new Widget(1L, WidgetType.ATTACHMENT);
        widgetRepository.save(widget);

        AttachmentWidget attachmentWidget = new AttachmentWidget(widget.getId());
        attachmentWidgetRepository.save(attachmentWidget);

        entityManager.flush();
        entityManager.clear();

        AttachmentWidgetDeleteRequest request = AttachmentWidgetDeleteRequest.from(attachmentWidget.getId());

        // when
        attachmentWidgetService.deleteAttachmentWidget(request);

        // then
        assertThat(attachmentWidgetRepository.findById(attachmentWidget.getId())).isEmpty();
        assertThat(widgetRepository.findById(widget.getId())).isEmpty();
    }
}
