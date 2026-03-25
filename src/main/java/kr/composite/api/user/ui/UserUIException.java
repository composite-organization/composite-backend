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

    public static UserUIException userNotFound(Long userId) {
        return new UserUIException(
                generateCode(2),
                "사용자 정보가 존재하지 않습니다.",
                "제공된 사용자 ID: " + userId,
                Category.NOT_FOUND
        );
    }

    public static UserUIException memberNotFound(Long userId) {
        return new UserUIException(
                generateCode(3),
                "회원 정보가 존재하지 않습니다.",
                "제공된 사용자 ID: " + userId,
                Category.NOT_FOUND
        );
    }

    public static UserUIException guestNotFound(Long userId) {
        return new UserUIException(
                generateCode(4),
                "게스트 정보가 존재하지 않습니다.",
                "제공된 사용자 ID: " + userId,
                Category.NOT_FOUND
        );
    }

    public static UserUIException unsupportedUserType(String typeName) {
        return new UserUIException(
                generateCode(5),
                "지원하지 않는 사용자 타입입니다.",
                "요청된 타입: " + typeName,
                Category.VALIDATION
        );
    }

    private static String generateCode(int number) {
        return CODE_PREFIX + String.format("%03d", number);
    }
}