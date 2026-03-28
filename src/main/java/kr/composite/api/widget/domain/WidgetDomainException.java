package kr.composite.api.widget.domain;

import kr.composite.api.exception.domain.BusinessException;

class WidgetDomainException extends BusinessException {

    private static final String CODE_PREFIX = "WIDGET_DOMAIN_";

    private WidgetDomainException(String code, String message, String detail, Category category) {
        super(code, message, detail, category);
    }

    public static WidgetDomainException unsupportedType(String description) {
        return new WidgetDomainException(
                generateCode(1),
                "지원하지 않는 위젯 타입입니다.",
                "제공된 타입: " + description,
                Category.VALIDATION
        );
    }

    private static String generateCode(int number) {
        return CODE_PREFIX + String.format("%03d", number);
    }
}
