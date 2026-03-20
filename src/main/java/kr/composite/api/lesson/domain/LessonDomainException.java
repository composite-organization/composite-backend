package kr.composite.api.lesson.domain;

import kr.composite.api.exception.domain.BusinessException;

public class LessonDomainException extends BusinessException {

    private static final String CODE_PREFIX = "LESSON_DOMAIN_";

    private LessonDomainException(String code, String message, Category category) {
        super(code, message, category);
    }

    private LessonDomainException(String code, String message, String detail, Category category) {
        super(code, message, detail, category);
    }

    public static LessonDomainException emptyCode(String code) {
        return new LessonDomainException(
                generateCode(1),
                "수업 코드가 비어있습니다.",
                "제공된 코드: " + code,
                Category.VALIDATION
        );
    }

    public static LessonDomainException emptyName() {
        return new LessonDomainException(
                generateCode(2),
                "수업 이름이 비어있습니다.",
                Category.VALIDATION
        );
    }

    public static LessonDomainException invalidNameLength(String name, int min, int max) {
        return new LessonDomainException(
                generateCode(3),
                "수업 이름의 길이가 유효하지 않습니다. (" + min + "~" + max + "자)",
                "제공된 이름: " + name + " (길이: " + name.length() + ")",
                Category.VALIDATION
        );
    }

    public static LessonDomainException emptyPassword() {
        return new LessonDomainException(
                generateCode(4),
                "수업 비밀번호가 비어있습니다.",
                Category.VALIDATION
        );
    }

    public static LessonDomainException invalidPasswordLength(int min, int max) {
        return new LessonDomainException(
                generateCode(5),
                "수업 비밀번호의 길이가 유효하지 않습니다. (" + min + "~" + max + "자)",
                Category.VALIDATION
        );
    }

    public static LessonDomainException invalidPasswordPattern() {
        return new LessonDomainException(
                generateCode(6),
                "수업 비밀번호 형식이 유효하지 않습니다.",
                Category.VALIDATION
        );
    }

    private static String generateCode(int number) {
        return CODE_PREFIX + String.format("%03d", number);
    }
}
