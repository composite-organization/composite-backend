package kr.composite.api.lesson.ui;

import kr.composite.api.exception.domain.BusinessException;

public class LessonUIException extends BusinessException {

    private static final String CODE_PREFIX = "LESSON_UI_";

    private LessonUIException(String code, String message, Category category) {
        super(code, message, category);
    }

    private LessonUIException(String code, String message, String detail, Category category) {
        super(code, message, detail, category);
    }

    private static String generateCode(int number) {
        return CODE_PREFIX + String.format("%03d", number);
    }
}
