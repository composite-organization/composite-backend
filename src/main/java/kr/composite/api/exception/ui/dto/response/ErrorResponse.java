package kr.composite.api.exception.ui.dto.response;

import kr.composite.api.exception.domain.BusinessException;
import kr.composite.api.exception.ui.WebExceptionType;

public record ErrorResponse(
        String code,
        String message,
        String detail
) {

    public static ErrorResponse from(BusinessException businessException) {
        return new ErrorResponse(
                businessException.getCode(),
                businessException.getMessage(),
                businessException.getDetail()
        );
    }

    public static ErrorResponse from(Exception exception) {
        return WebExceptionType.resolve(exception)
                .map(webExceptionType -> new ErrorResponse(
                        webExceptionType.getCode(),
                        webExceptionType.getMessage(),
                        webExceptionType.getDetailGenerator().apply(exception)
                ))
                .orElseGet(() -> new ErrorResponse(
                        "UNEXPECTED_ERROR",
                        "알 수 없는 문제가 발생했습니다.",
                        ""
                ));
    }
}
