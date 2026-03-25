package kr.composite.api.user.domain;

import kr.composite.api.exception.domain.BusinessException;

class UserDomainException extends BusinessException {

    private static final String CODE_PREFIX = "USER_DOMAIN_";

    private UserDomainException(String code, String message, Category category) {
        super(code, message, category);
    }

    private UserDomainException(String code, String message, String detail, Category category) {
        super(code, message, detail, category);
    }

    public static UserDomainException emptyName() {
        return new UserDomainException(
                generateCode(1),
                "사용자 이름이 비어있습니다.",
                Category.VALIDATION
        );
    }

    public static UserDomainException invalidNameLength(String name, int minLength, int maxLength) {
        return new UserDomainException(
                generateCode(2),
                "사용자 이름의 길이가 유효하지 않습니다. (최소 " + minLength + "자, 최대 " + maxLength + "자)",
                "제공된 이름: " + name + " (길이: " + name.length() + ")",
                Category.VALIDATION
        );
    }

    private static String generateCode(int number) {
        return CODE_PREFIX + String.format("%03d", number);
    }
}
