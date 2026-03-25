package kr.composite.api.exception.domain;

import lombok.Getter;

@Getter
public abstract class BusinessException extends RuntimeException {

    private final String code;
    private final String message;
    private final String detail;
    private final Category category;

    protected BusinessException(String code, String message, Category category) {
        super(message);
        this.code = code;
        this.message = message;
        this.detail = null;
        this.category = category;
    }

    protected BusinessException(String code, String message, Category category, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.message = message;
        this.detail = null;
        this.category = category;
    }

    protected BusinessException(String code, String message, String detail, Category category) {
        super(message);
        this.code = code;
        this.message = message;
        this.detail = detail;
        this.category = category;
    }

    public enum Category {

        // 유효성 검증 실패
        VALIDATION,

        // 인증 실패
        UNAUTHORIZED,

        // 권한 부족
        FORBIDDEN,

        // 리소스 없음
        NOT_FOUND,

        // 리소스 충돌
        CONFLICT,

        // 내부 오류
        INTERNAL_SERVER_ERROR,

        // 외부 서비스 오류
        EXTERNAL_SERVICE_ERROR
    }
}
