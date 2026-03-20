package kr.composite.exception.ui.dto.response;

import kr.composite.exception.domain.BusinessException;
import kr.composite.exception.ui.WebExceptionType;
import org.springframework.http.HttpStatus;

public record ErrorResponse(
        String code,
        String message,
        String detail,
        HttpStatus httpStatus
) {

    public static ErrorResponse from(BusinessException businessException) {
        BusinessException.Category category = businessException.getCategory();
        HttpStatus httpStatus = switch (category) {
            case VALIDATION -> HttpStatus.BAD_REQUEST;
            case UNAUTHORIZED -> HttpStatus.UNAUTHORIZED;
            case FORBIDDEN -> HttpStatus.FORBIDDEN;
            case NOT_FOUND -> HttpStatus.NOT_FOUND;
            case CONFLICT -> HttpStatus.CONFLICT;
            case EXTERNAL_SERVICE_ERROR -> HttpStatus.BAD_GATEWAY;
            case INTERNAL_SERVER_ERROR -> HttpStatus.INTERNAL_SERVER_ERROR;
        };
        return new ErrorResponse(
                businessException.getCode(),
                businessException.getMessage(),
                businessException.getDetail(),
                httpStatus
        );
    }

    public static ErrorResponse from(Exception exception) {
        return WebExceptionType.resolve(exception)
                .map(webExceptionType -> new ErrorResponse(
                        webExceptionType.getCode(),
                        webExceptionType.getMessage(),
                        webExceptionType.getDetailGenerator().apply(exception),
                        webExceptionType.getHttpStatus()
                ))
                .orElseGet(() -> new ErrorResponse(
                        "UNEXPECTED_ERROR",
                        "알 수 없는 문제가 발생했습니다.",
                        "",
                        HttpStatus.INTERNAL_SERVER_ERROR
                ));
    }
}
