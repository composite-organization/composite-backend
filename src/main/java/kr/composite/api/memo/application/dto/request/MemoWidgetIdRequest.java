package kr.composite.api.memo.application.dto.request;

public record MemoWidgetIdRequest(
        Long id
) {

    public static MemoWidgetIdRequest from(Long id) {
        return new MemoWidgetIdRequest(id);
    }
}
