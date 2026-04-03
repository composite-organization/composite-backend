package kr.composite.api.quiz.domain;

import kr.composite.api.exception.domain.BusinessException;

class QuizWidgetDomainException extends BusinessException {

    private static final String CODE_PREFIX = "QUIZ_DOMAIN_";

    private QuizWidgetDomainException(String code, String message, Category category) {
        super(code, message, category);
    }

    private QuizWidgetDomainException(String code, String message, String detail, Category category) {
        super(code, message, detail, category);
    }

    public static QuizWidgetDomainException emptyTitle() {
        return new QuizWidgetDomainException(
                generateCode(1),
                "퀴즈 제목이 비어있습니다.",
                Category.VALIDATION
        );
    }

    public static QuizWidgetDomainException invalidTitleLength(String title, int minLength, int maxLength) {
        return new QuizWidgetDomainException(
                generateCode(2),
                "퀴즈 제목의 길이가 유효하지 않습니다. (최소 " + minLength + "자, 최대 " + maxLength + "자)",
                "제공된 제목: " + title + " (길이: " + title.length() + ")",
                Category.VALIDATION
        );
    }

    public static QuizWidgetDomainException unsupportedStatus(String description) {
        return new QuizWidgetDomainException(
                generateCode(3),
                "지원하지 않는 퀴즈 상태입니다.",
                "제공된 상태: " + description,
                Category.VALIDATION
        );
    }

    private static String generateCode(int number) {
        return CODE_PREFIX + String.format("%03d", number);
    }
}
