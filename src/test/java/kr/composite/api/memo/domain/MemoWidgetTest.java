package kr.composite.api.memo.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class MemoWidgetTest {

    @Test
    void 메모를_생성할_수_있다() {
        // given
        Long widgetId = 1L;
        MemoTitle title = new MemoTitle("메모 제목");
        MemoContent content = new MemoContent("메모 내용");

        // when
        MemoWidget memoWidget = new MemoWidget(widgetId, title, content);

        // then
        assertThat(memoWidget).isNotNull();
        assertThat(memoWidget.getWidgetId()).isEqualTo(widgetId);
        assertThat(memoWidget.getTitle()).isEqualTo(title);
        assertThat(memoWidget.getContent()).isEqualTo(content);
    }

    @Test
    void 메모를_수정할_수_있다() {
        // given
        Long widgetId = 1L;
        MemoTitle title = new MemoTitle("메모 제목");
        MemoContent content = new MemoContent("메모 내용");
        MemoWidget memoWidget = new MemoWidget(widgetId, title, content);

        String updateTitle = "메모 제목 업데이트";
        String updateContent = "메모 내용 업데이트";

        // when
        memoWidget.update(updateTitle, updateContent);

        // then
        assertThat(memoWidget.getTitle().getValue()).isEqualTo(updateTitle);
        assertThat(memoWidget.getContent().getValue()).isEqualTo(updateContent);
    }
}
