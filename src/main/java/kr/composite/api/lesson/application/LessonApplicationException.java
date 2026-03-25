package kr.composite.api.lesson.application;

import kr.composite.api.exception.domain.BusinessException;

class LessonApplicationException extends BusinessException {

    private static final String CODE_PREFIX = "LESSON_APPLICATION_";

    private LessonApplicationException(String code, String message, Category category) {
        super(code, message, category);
    }

    private LessonApplicationException(String code, String message, String detail, Category category) {
        super(code, message, detail, category);
    }

    public static LessonApplicationException alreadyJoined() {
        return new LessonApplicationException(
                generateCode(1),
                "이미 참여한 사용자입니다.",
                Category.CONFLICT
        );
    }

    private static String generateCode(int number) {
        return CODE_PREFIX + String.format("%03d", number);
    }
}