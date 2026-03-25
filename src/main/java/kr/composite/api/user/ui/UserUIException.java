package kr.composite.api.user.ui;

import kr.composite.api.exception.domain.BusinessException;

public class UserUIException extends BusinessException {

    private static final String CODE_PREFIX = "USER_UI_";

    private UserUIException(String code, String message, Category category) {
        super(code, message, category);
    }

    private UserUIException(String code, String message, String detail, Category category) {
        super(code, message, detail, category);
    }

    public static UserUIException authRequired() {
        return new UserUIException(
                generateCode(1),
                "인증 정보가 필요한 요청입니다.",
                Category.UNAUTHORIZED
        );
    }

    public static UserUIException unsupportedUserType() {
        return new UserUIException(
                generateCode(5),
                "지원하지 않는 사용자 타입입니다.",
                Category.VALIDATION
        );
    }

    private static String generateCode(int number) {
        return CODE_PREFIX + String.format("%03d", number);
    }
}
