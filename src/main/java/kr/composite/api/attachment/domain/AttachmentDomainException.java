package kr.composite.api.attachment.domain;

import kr.composite.api.exception.domain.BusinessException;

class AttachmentDomainException extends BusinessException {

    private static final String CODE_PREFIX = "ATTACHMENT_DOMAIN_";

    private AttachmentDomainException(String code, String message, Category category) {
        super(code, message, category);
    }

    private AttachmentDomainException(String code, String message, String detail, Category category) {
        super(code, message, detail, category);
    }

    static AttachmentDomainException emptyName() {
        return new AttachmentDomainException(
                generateCode(1),
                "첨부파일 이름이 비어있습니다.",
                Category.VALIDATION
        );
    }

    static AttachmentDomainException invalidNameLength(String name, int minLength) {
        return new AttachmentDomainException(
                generateCode(2),
                "첨부파일 이름의 길이가 유효하지 않습니다. (최소 " + minLength + "자 이상)",
                "제공된 이름: " + name + " (길이: " + name.length() + ")",
                Category.VALIDATION
        );
    }

    static AttachmentDomainException invalidUnitDescription(String description) {
        return new AttachmentDomainException(
                generateCode(3),
                "첨부파일 단위가 유효하지 않습니다.",
                "제공된 단위: " + description,
                Category.VALIDATION
        );
    }

    private static String generateCode(int number) {
        return CODE_PREFIX + String.format("%03d", number);
    }
}
