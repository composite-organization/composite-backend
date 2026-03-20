package kr.composite.api.exception.ui;

import kr.composite.api.exception.domain.BusinessException;
import kr.composite.api.exception.ui.dto.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handle(final BusinessException exception) {
        ErrorResponse response = ErrorResponse.from(exception);
        log.error("비즈니스 예외 {} 발생", response.code(), exception);
        return ResponseEntity.status(response.httpStatus()).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handle(final Exception exception) {
        ErrorResponse response = ErrorResponse.from(exception);
        log.error("예외 {} 발생", response.code(), exception);
        return ResponseEntity.status(response.httpStatus()).body(response);
    }
}
