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

    public static LessonApplicationException cannotFindTeacher() {
        return new LessonApplicationException(
                generateCode(2),
                "수업자를 찾을 수 없습니다.",
                Category.NOT_FOUND
        );
    }

    public static LessonApplicationException noPermission() {
        return new LessonApplicationException(
                generateCode(3),
                "수업 접근 권한이 없습니다.",
                Category.UNAUTHORIZED
        );
    }

    public static LessonApplicationException cannotFindLesson() {
        return new LessonApplicationException(
                generateCode(4),
                "수업을 찾을 수 없습니다.",
                Category.NOT_FOUND
        );
    }

    private static String generateCode(int number) {
        return CODE_PREFIX + String.format("%03d", number);
    }
}
