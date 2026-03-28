package kr.composite.api.attachment.application;

import kr.composite.api.exception.domain.BusinessException;

class AttachmentApplicationException extends BusinessException {

    private static final String CODE_PREFIX = "ATTACHMENT_APP_";

    private AttachmentApplicationException(String code, String message, Category category) {
        super(code, message, category);
    }

    private AttachmentApplicationException(String code, String message, String detail, Category category) {
        super(code, message, detail, category);
    }

    public static AttachmentApplicationException widgetNotFound() {
        return new AttachmentApplicationException(
                generateCode(1),
                "자료 공유 위젯을 찾을 수 없습니다.",
                Category.NOT_FOUND
        );
    }

    public static AttachmentApplicationException fileReadFailed() {
        return new AttachmentApplicationException(
                generateCode(2),
                "파일 읽기 중 오류가 발생했습니다.",
                Category.INTERNAL_SERVER_ERROR
        );
    }

    public static AttachmentApplicationException fileSizeExceeded(long actual, long max) {
        return new AttachmentApplicationException(
                generateCode(3),
                "파일 크기가 너무 큽니다.",
                "실제 크기: " + actual + " bytes, 최대 허용 크기: " + max + " bytes",
                Category.VALIDATION
        );
    }

    public static AttachmentApplicationException attachmentNotFound() {
        return new AttachmentApplicationException(
                generateCode(4),
                "자료를 찾을 수 없습니다.",
                Category.NOT_FOUND
        );
    }

    private static String generateCode(int number) {
        return CODE_PREFIX + String.format("%03d", number);
    }
}
