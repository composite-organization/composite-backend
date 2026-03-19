package kr.composite.api.memo.application.dto.request;

public record MemoWidgetUpdateRequest(
        Long memoWidgetId,
        String title,
        String content
) {

}
