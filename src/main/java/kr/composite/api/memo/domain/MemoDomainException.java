package kr.composite.api.memo.domain;

import kr.composite.api.exception.domain.BusinessException;

class MemoDomainException extends BusinessException {

    private static final String CODE_PREFIX = "MEMO_DOMAIN_";

    private MemoDomainException(String code, String message, Category category) {
        super(code, message, category);
    }

    private MemoDomainException(String code, String message, String detail, Category category) {
        super(code, message, detail, category);
    }

    public static MemoDomainException emptyTitle() {
        return new MemoDomainException(
                generateCode(1),
                "메모 제목이 비어있습니다.",
                null,
                Category.VALIDATION
        );
    }

    public static MemoDomainException invalidTitleLength(int maxLength) {
        return new MemoDomainException(
                generateCode(2),
                "메모 제목의 길이가 유효하지 않습니다. (최대 " + maxLength + "자)",
                Category.VALIDATION
        );
    }

    public static MemoDomainException emptyContent() {
        return new MemoDomainException(
                generateCode(3),
                "메모 내용이 비어있습니다.",
                null,
                Category.VALIDATION
        );
    }

    public static MemoDomainException invalidContentLength(int maxLength) {
        return new MemoDomainException(
                generateCode(4),
                "메모 내용의 길이가 유효하지 않습니다. (최대 " + maxLength + "자)",
                Category.VALIDATION
        );
    }

    private static String generateCode(int number) {
        return CODE_PREFIX + String.format("%03d", number);
    }
}
