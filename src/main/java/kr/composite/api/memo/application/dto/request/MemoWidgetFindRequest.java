package kr.composite.api.memo.application.dto.request;

public record MemoWidgetFindRequest(
        Long id
) {

    public static MemoWidgetFindRequest from(Long id) {
        return new MemoWidgetFindRequest(id);
    }
}
