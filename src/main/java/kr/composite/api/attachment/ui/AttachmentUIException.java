package kr.composite.api.attachment.ui;

import kr.composite.api.exception.domain.BusinessException;

public class AttachmentUIException extends BusinessException {

    private static final String CODE_PREFIX = "ATTACHMENT_UI_";

    private AttachmentUIException(String code, String message, Category category) {
        super(code, message, category);
    }

    public static AttachmentUIException fileStreamReadFailed() {
        return new AttachmentUIException(
                generateCode(1),
                "파일 스트림을 읽는 중 오류가 발생했습니다.",
                Category.VALIDATION
        );
    }

    private static String generateCode(int number) {
        return CODE_PREFIX + String.format("%03d", number);
    }
}
