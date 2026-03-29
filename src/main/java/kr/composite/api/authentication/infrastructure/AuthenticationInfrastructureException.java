package kr.composite.api.authentication.infrastructure;

import kr.composite.api.exception.domain.BusinessException;

class AuthenticationInfrastructureException extends BusinessException {

    private static final String CODE_PREFIX = "AUTHENTICATION_INFRASTRUCTURE_";

    private AuthenticationInfrastructureException(String code, String message, String detail, Category category) {
        super(code, message, detail, category);
    }

    private AuthenticationInfrastructureException(String code, String message, Category category, Throwable cause) {
        super(code, message, category, cause);
    }

    static AuthenticationInfrastructureException expiredCredential(Throwable cause) {
        return new AuthenticationInfrastructureException(
                generateCode(1),
                "만료된 인증 정보입니다.",
                cause.getMessage(),
                Category.UNAUTHORIZED
        );
    }

    static AuthenticationInfrastructureException invalidCredential(Throwable cause) {
        return new AuthenticationInfrastructureException(
                generateCode(2),
                "유효하지 않은 인증 정보입니다.",
                cause.getMessage(),
                Category.UNAUTHORIZED
        );
    }

    static AuthenticationInfrastructureException unknown(Throwable cause) {
        return new AuthenticationInfrastructureException(
                generateCode(3),
                "알 수 없는 이유로 인증 정보 처리에 실패했습니다.",
                Category.INTERNAL_SERVER_ERROR,
                cause
        );
    }

    private static String generateCode(int number) {
        return CODE_PREFIX + String.format("%03d", number);
    }
}
