package kr.composite.api.participant.domain;

import kr.composite.api.exception.domain.BusinessException;

public class StudentDomainException extends BusinessException {

    private static final String CODE_PREFIX = "STUDENT_DOMAIN_";

    private StudentDomainException(String code, String message, Category category) {
        super(code, message, category);
    }

    private StudentDomainException(String code, String message, String detail, Category category) {
        super(code, message, detail, category);
    }

    public static StudentDomainException emptyName() {
        return new StudentDomainException(
                generateCode(1),
                "학생 이름이 비어있습니다.",
                Category.VALIDATION
        );
    }

    public static StudentDomainException invalidNameLength(String name, int minLength, int maxLength) {
        return new StudentDomainException(
                generateCode(2),
                "학생 이름의 길이가 유효하지 않습니다. (최소 " + minLength + "자, 최대 " + maxLength + "자)",
                "제공된 이름: " + name + " (길이: " + name.length() + ")",
                Category.VALIDATION
        );
    }

    private static String generateCode(int number) {
        return CODE_PREFIX + String.format("%03d", number);
    }
}
