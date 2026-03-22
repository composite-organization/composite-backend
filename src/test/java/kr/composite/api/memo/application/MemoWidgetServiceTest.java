package kr.composite.api.memo.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import jakarta.persistence.EntityManager;
import kr.composite.api.memo.application.dto.request.MemoWidgetAddRequest;
import kr.composite.api.memo.application.dto.request.MemoWidgetFindRequest;
import kr.composite.api.memo.application.dto.request.MemoWidgetUpdateRequest;
import kr.composite.api.memo.application.dto.response.MemoWidgetResponse;
import kr.composite.api.memo.domain.MemoContent;
import kr.composite.api.memo.domain.MemoTitle;
import kr.composite.api.memo.domain.MemoWidget;
import kr.composite.api.memo.infrastructure.SpringDataJpaMemoWidgetRepository;
import kr.composite.api.widget.domain.Widget;
import kr.composite.api.widget.domain.WidgetType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class MemoWidgetServiceTest {

    @Autowired
    EntityManager entityManager;

    @Autowired
    SpringDataJpaMemoWidgetRepository springDataJpaMemoWidgetRepository;

    @Autowired
    private MemoWidgetService memoWidgetService;

    @Test
    void 메모를_조회할_수_있다() {
        // given
        Widget widget = new Widget(null, WidgetType.MEMO);
        entityManager.persist(widget);
        MemoTitle memoTitle = new MemoTitle("title");
        MemoContent memoContent = new MemoContent("content");
        MemoWidget memoWidget = new MemoWidget(widget.getId(), memoTitle, memoContent);
        entityManager.persist(memoWidget);

        entityManager.flush();
        entityManager.clear();

        // when
        MemoWidgetFindRequest memoWidgetFindRequest = MemoWidgetFindRequest.from(memoWidget.getId());
        MemoWidgetResponse memoWidgetResponse = memoWidgetService.getMemoWidget(memoWidgetFindRequest);

        // then
        assertThat(memoWidgetResponse.id()).isEqualTo(memoWidget.getId());
        assertThat(memoWidgetResponse.widgetId()).isEqualTo(widget.getId());
        assertThat(memoWidgetResponse.title()).isEqualTo("title");
        assertThat(memoWidgetResponse.content()).isEqualTo("content");
    }

    @Test
    void 존재하지_않는_메모를_조회시_예외가_발생한다() {
        // given
        MemoWidgetFindRequest memoWidgetFindRequest = MemoWidgetFindRequest.from(9999L);
        // when & then
        assertThatThrownBy(() -> memoWidgetService.getMemoWidget(memoWidgetFindRequest)).isInstanceOf(
                IllegalArgumentException.class);
    }

    @Test
    void 메모를_생성할_수_있다() {
        // given

        Long lessonId = 1L;
        String title = "메모 제목";
        String content = "메모 내용";
        MemoWidgetAddRequest memoWidgetAddRequest = new MemoWidgetAddRequest(lessonId, title, content);

        // when
        MemoWidgetResponse memoWidgetResponse = memoWidgetService.addMemoWidget(memoWidgetAddRequest);

        // then
        MemoWidget memoWidget = springDataJpaMemoWidgetRepository.findById(memoWidgetResponse.id()).orElse(null);

        assertThat(memoWidget).isNotNull();
        assertThat(memoWidget.getTitle().getValue()).isEqualTo(title);
        assertThat(memoWidget.getContent().getValue()).isEqualTo(content);
    }

    @Test
    void 메모를_수정할_수_있다() {
        // given
        String title = "메모 제목";
        String content = "메모 내용";
        Widget widget = new Widget(null, WidgetType.MEMO);
        entityManager.persist(widget);
        MemoTitle memoTitle = new MemoTitle(title);
        MemoContent memoContent = new MemoContent(content);
        MemoWidget memoWidget = new MemoWidget(widget.getId(), memoTitle, memoContent);
        entityManager.persist(memoWidget);

        entityManager.flush();
        entityManager.clear();

        String updateTitle = "메모 제목 업데이트";
        String updatedContent = "메모 내용 업데이트";
        MemoWidgetUpdateRequest memoWidgetUpdateRequest = new MemoWidgetUpdateRequest(memoWidget.getId(), updateTitle,
                updatedContent);

        // when
        MemoWidgetResponse memoWidgetResponse = memoWidgetService.updateMemoWidget(memoWidgetUpdateRequest);

        // then
        MemoWidget updatedMemoWidget = springDataJpaMemoWidgetRepository.findById(memoWidgetResponse.id()).orElse(null);

        assertThat(updatedMemoWidget).isNotNull();
        assertThat(updatedMemoWidget.getId()).isEqualTo(memoWidget.getId());
        assertThat(updatedMemoWidget.getTitle().getValue()).isEqualTo(updateTitle);
        assertThat(updatedMemoWidget.getContent().getValue()).isEqualTo(updatedContent);
    }

    @Test
    void 메모를_삭제할_수_있다() {
        // given
        Widget widget = new Widget(null, WidgetType.MEMO);
        entityManager.persist(widget);
        String title = "메모 제목";
        String content = "메모 내용";
        MemoTitle memoTitle = new MemoTitle(title);
        MemoContent memoContent = new MemoContent(content);
        MemoWidget memoWidget = new MemoWidget(widget.getId(), memoTitle, memoContent);
        entityManager.persist(memoWidget);

        entityManager.flush();
        entityManager.clear();

        MemoWidgetFindRequest memoWidgetFindRequest = MemoWidgetFindRequest.from(memoWidget.getId());

        // when
        memoWidgetService.deleteMemoWidget(memoWidgetFindRequest);

        // then
        assertThat(springDataJpaMemoWidgetRepository.findById(memoWidget.getId())).isEmpty();
    }
}
