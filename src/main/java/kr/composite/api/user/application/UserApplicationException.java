package kr.composite.api.user.application;

import kr.composite.api.exception.domain.BusinessException;

class UserApplicationException extends BusinessException {

    private static final String CODE_PREFIX = "USER_APPLICATION_";

    private UserApplicationException(String code, String message, Category category) {
        super(code, message, category);
    }

    private UserApplicationException(String code, String message, String detail, Category category) {
        super(code, message, detail, category);
    }

    public static UserApplicationException userNotFound() {
        return new UserApplicationException(
                generateCode(1),
                "사용자 정보가 존재하지 않습니다.",
                Category.NOT_FOUND
        );
    }

    public static UserApplicationException memberNotFound() {
        return new UserApplicationException(
                generateCode(2),
                "회원 정보가 존재하지 않습니다.",
                Category.NOT_FOUND
        );
    }

    public static UserApplicationException guestNotFound() {
        return new UserApplicationException(
                generateCode(3),
                "게스트 정보가 존재하지 않습니다.",
                Category.NOT_FOUND
        );
    }


    private static String generateCode(int number) {
        return CODE_PREFIX + String.format("%03d", number);
    }
}
