package kr.composite.api.memo.application.dto.request;

public record MemoWidgetAddRequest(
        Long lessonId,
        String title,
        String content
) {

}
