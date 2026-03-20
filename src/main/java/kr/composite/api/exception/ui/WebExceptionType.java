package kr.composite.api.exception.ui;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Getter
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public enum WebExceptionType {

    METHOD_NOT_ALLOWED(
            "WEB-001",
            "지원하지 않는 HTTP 메서드입니다.",
            HttpStatus.METHOD_NOT_ALLOWED,
            HttpRequestMethodNotSupportedException.class,
            exception -> ((HttpRequestMethodNotSupportedException) exception).getMethod()
    ),
    UNSUPPORTED_MEDIA_TYPE(
            "WEB-002",
            "지원하지 않는 미디어 타입입니다.",
            HttpStatus.UNSUPPORTED_MEDIA_TYPE,
            HttpMediaTypeNotSupportedException.class,
            exception -> ""
    ),
    NOT_ACCEPTABLE(
            "WEB-003",
            "수용 가능한 응답 형식이 없습니다.",
            HttpStatus.NOT_ACCEPTABLE,
            HttpMediaTypeNotAcceptableException.class,
            exception -> ((HttpMediaTypeNotAcceptableException) exception).getSupportedMediaTypes().toString()
    ),
    MISSING_PATH_VARIABLE(
            "WEB-004",
            "필수 경로 변수가 누락되었습니다.",
            HttpStatus.INTERNAL_SERVER_ERROR,
            MissingPathVariableException.class,
            exception -> ""
    ),
    MISSING_PARAMETER(
            "WEB-005",
            "필수 쿼리 파라미터가 누락되었습니다.",
            HttpStatus.BAD_REQUEST,
            MissingServletRequestParameterException.class,
            exception -> ""
    ),
    REQUEST_BINDING_ERROR(
            "WEB-006",
            "요청 바인딩 중 오류가 발생했습니다.",
            HttpStatus.BAD_REQUEST,
            ServletRequestBindingException.class,
            exception -> ""
    ),
    ARGUMENT_NOT_VALID(
            "WEB-007",
            "입력값 검증에 실패했습니다.",
            HttpStatus.BAD_REQUEST,
            MethodArgumentNotValidException.class,
            exception -> ""
    ),
    HANDLER_VALIDATION_ERROR(
            "WEB-008",
            "파라미터 검증에 실패했습니다.",
            HttpStatus.BAD_REQUEST,
            HandlerMethodValidationException.class,
            exception -> ""
    ),
    NOT_FOUND(
            "WEB-009",
            "요청한 리소스를 찾을 수 없습니다.",
            HttpStatus.NOT_FOUND,
            NoResourceFoundException.class,
            exception -> ((NoResourceFoundException) exception).getResourcePath()
    ),
    ASYNC_TIMEOUT(
            "WEB-010",
            "비동기 요청 시간이 초과되었습니다.",
            HttpStatus.SERVICE_UNAVAILABLE,
            AsyncRequestTimeoutException.class,
            exception -> ""
    ),
    TYPE_MISMATCH(
            "WEB-011",
            "파라미터 타입이 일치하지 않습니다.",
            HttpStatus.BAD_REQUEST,
            TypeMismatchException.class,
            exception -> ""
    ),
    METHOD_ARGUMENT_TYPE_MISMATCH(
            "WEB-012",
            "메서드 인자의 타입이 일치하지 않습니다.",
            HttpStatus.BAD_REQUEST,
            MethodArgumentTypeMismatchException.class,
            exception -> ""
    ),
    MESSAGE_NOT_READABLE(
            "WEB-013",
            "요청 메시지를 읽을 수 없습니다.",
            HttpStatus.BAD_REQUEST,
            HttpMessageNotReadableException.class,
            exception -> ""
    ),
    MESSAGE_NOT_WRITABLE(
            "WEB-014",
            "응답 메시지를 생성할 수 없습니다.",
            HttpStatus.INTERNAL_SERVER_ERROR,
            HttpMessageNotWritableException.class,
            exception -> ""
    );

    private static final Map<Class<? extends Exception>, WebExceptionType> CACHE =
            Arrays.stream(values())
                    .collect(Collectors.toMap(WebExceptionType::getTargetException, Function.identity()));

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;
    private final Class<? extends Exception> targetException;
    private final Function<Exception, String> detailGenerator;

    public static Optional<WebExceptionType> resolve(Exception exception) {
        return Optional.ofNullable(CACHE.get(exception.getClass()));
    }
}
