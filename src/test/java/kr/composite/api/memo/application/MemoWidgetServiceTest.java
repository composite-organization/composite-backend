package kr.composite.api.memo.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kr.composite.api.memo.application.dto.request.MemoWidgetAddRequest;
import kr.composite.api.memo.application.dto.request.MemoWidgetIdRequest;
import kr.composite.api.memo.application.dto.request.MemoWidgetUpdateRequest;
import kr.composite.api.memo.application.dto.response.MemoWidgetResponse;
import kr.composite.api.memo.domain.MemoContent;
import kr.composite.api.memo.domain.MemoTitle;
import kr.composite.api.memo.domain.MemoWidget;
import kr.composite.api.memo.infrastructure.JpaMemoWidgetRepository;
import kr.composite.api.memo.infrastructure.SpringDataJpaMemoWidgetRepository;
import kr.composite.api.widget.domain.Widget;
import kr.composite.api.widget.domain.WidgetType;
import kr.composite.api.widget.infrastructure.JpaWidgetRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(value = {MemoWidgetService.class, JpaMemoWidgetRepository.class, JpaWidgetRepository.class})
class MemoWidgetServiceTest {

    @Autowired
    TestEntityManager testEntityManager;

    @Autowired
    SpringDataJpaMemoWidgetRepository springDataJpaMemoWidgetRepository;

    @Autowired
    private MemoWidgetService memoWidgetService;

    @Test
    void 메모를_조회할_수_있다() {
        // given
        testEntityManager.persist(new Widget(1L, WidgetType.MEMO));
        MemoTitle memoTitle = new MemoTitle("title");
        MemoContent memoContent = new MemoContent("content");
        testEntityManager.persist(new MemoWidget(1L, memoTitle, memoContent));

        testEntityManager.flush();
        testEntityManager.clear();

        // when
        MemoWidgetIdRequest memoWidgetIdRequest = MemoWidgetIdRequest.from(1L);
        MemoWidgetResponse memoWidgetResponse = memoWidgetService.getMemoWidget(memoWidgetIdRequest);

        // then
        assertThat(memoWidgetResponse.id()).isEqualTo(1L);
        assertThat(memoWidgetResponse.widgetId()).isEqualTo(1L);
        assertThat(memoWidgetResponse.title()).isEqualTo("title");
        assertThat(memoWidgetResponse.content()).isEqualTo("content");
    }

    @Test
    void 존재하지_않는_메모를_조회시_예외가_발생한다() {
        // given
        MemoWidgetIdRequest memoWidgetIdRequest = MemoWidgetIdRequest.from(1L);
        // when & then
        assertThatThrownBy(() -> memoWidgetService.getMemoWidget(memoWidgetIdRequest)).isInstanceOf(
                IllegalArgumentException.class);
    }

    @Test
    void 메모를_생성할_수_있다() {
        // given
        String title = "메모 제목";
        String content = "메모 내용";
        MemoWidgetAddRequest memoWidgetAddRequest = new MemoWidgetAddRequest(1L, title, content);

        // when
        MemoWidgetResponse memoWidgetResponse = memoWidgetService.addMemoWidget(memoWidgetAddRequest);

        // then
        MemoWidget memoWidget = springDataJpaMemoWidgetRepository.findById(memoWidgetResponse.id()).orElse(null);

        assertThat(memoWidget).isNotNull();
        assertThat(memoWidget.getWidgetId()).isEqualTo(1L);
        assertThat(memoWidget.getTitle().getValue()).isEqualTo(title);
        assertThat(memoWidget.getContent().getValue()).isEqualTo(content);
    }

    @Test
    void 메모를_수정할_수_있다() {
        // given
        String title = "메모 제목";
        String content = "메모 내용";
        testEntityManager.persist(new Widget(1L, WidgetType.MEMO));
        MemoTitle memoTitle = new MemoTitle(title);
        MemoContent memoContent = new MemoContent(content);
        testEntityManager.persist(new MemoWidget(1L, memoTitle, memoContent));

        testEntityManager.flush();
        testEntityManager.clear();

        String updateTitle = "메모 제목 업데이트";
        String updatedContent = "메모 내용 업데이트";
        MemoWidgetUpdateRequest memoWidgetUpdateRequest = new MemoWidgetUpdateRequest(1L, updateTitle, updatedContent);

        // when
        MemoWidgetResponse memoWidgetResponse = memoWidgetService.updateMemoWidget(memoWidgetUpdateRequest);

        // then
        MemoWidget memoWidget = springDataJpaMemoWidgetRepository.findById(memoWidgetResponse.id()).orElse(null);

        assertThat(memoWidget).isNotNull();
        assertThat(memoWidget.getId()).isEqualTo(1L);
        assertThat(memoWidget.getTitle().getValue()).isEqualTo(updateTitle);
        assertThat(memoWidget.getContent().getValue()).isEqualTo(updatedContent);
    }

    @Test
    void 메모를_삭제할_수_있다() {
        // given
        Long id = 1L;
        String title = "메모 제목";
        String content = "메모 내용";
        testEntityManager.persist(new Widget(id, WidgetType.MEMO));
        MemoTitle memoTitle = new MemoTitle(title);
        MemoContent memoContent = new MemoContent(content);
        testEntityManager.persist(new MemoWidget(id, memoTitle, memoContent));

        testEntityManager.flush();
        testEntityManager.clear();

        MemoWidgetIdRequest memoWidgetIdRequest = MemoWidgetIdRequest.from(id);

        // when
        memoWidgetService.deleteMemoWidget(memoWidgetIdRequest);

        // then
        assertThat(springDataJpaMemoWidgetRepository.findById(id)).isEmpty();
    }
}
