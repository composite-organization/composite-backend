package kr.composite.api.memo.application;

import kr.composite.api.exception.domain.BusinessException;

class MemoApplicationException extends BusinessException {

    private static final String CODE_PREFIX = "MEMO_APP_";

    private MemoApplicationException(String code, String message, Category category) {
        super(code, message, category);
    }

    public static MemoApplicationException widgetNotFound() {
        return new MemoApplicationException(
                generateCode(1),
                "메모 위젯을 찾을 수 없습니다.",
                Category.NOT_FOUND
        );
    }

    private static String generateCode(int number) {
        return CODE_PREFIX + String.format("%03d", number);
    }
}
