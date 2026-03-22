package kr.composite.api.exception.ui;

import kr.composite.api.exception.domain.BusinessException;
import kr.composite.api.exception.ui.dto.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handle(final BusinessException exception) {
        ErrorResponse response = ErrorResponse.from(exception);
        HttpStatus status = resolveStatus(exception.getCategory());
        log.error("비즈니스 예외 {} 발생", response.code(), exception);

        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handle(final Exception exception) {
        ErrorResponse response = ErrorResponse.from(exception);
        HttpStatus status = WebExceptionType.resolve(exception)
                .map(WebExceptionType::getHttpStatus)
                .orElse(HttpStatus.INTERNAL_SERVER_ERROR);
        log.error("예외 {} 발생", response.code(), exception);

        return ResponseEntity.status(status).body(response);
    }

    private HttpStatus resolveStatus(BusinessException.Category category) {
        return switch (category) {
            case VALIDATION -> HttpStatus.BAD_REQUEST;
            case UNAUTHORIZED -> HttpStatus.UNAUTHORIZED;
            case FORBIDDEN -> HttpStatus.FORBIDDEN;
            case NOT_FOUND -> HttpStatus.NOT_FOUND;
            case CONFLICT -> HttpStatus.CONFLICT;
            case EXTERNAL_SERVICE_ERROR -> HttpStatus.BAD_GATEWAY;
            case INTERNAL_SERVER_ERROR -> HttpStatus.INTERNAL_SERVER_ERROR;
        };
    }
}
