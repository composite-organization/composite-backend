package kr.composite.api.quiz.application;

import kr.composite.api.exception.domain.BusinessException;

public class QuizWidgetApplicationException extends BusinessException {

    private static final String CODE_PREFIX = "QUIZ_WIDGET_APPLICATION_";

    private QuizWidgetApplicationException(String code, String message, Category category) {
        super(code, message, category);
    }

    private QuizWidgetApplicationException(String code, String message, String detail, Category category) {
        super(code, message, detail, category);
    }

    public static QuizWidgetApplicationException cannotFindQuizWidget() {
        return new QuizWidgetApplicationException(
                generateCode(1),
                "퀴즈 위젯을 찾을 수 없습니다.",
                Category.NOT_FOUND
        );
    }

    public static QuizWidgetApplicationException cannotFindQuizOption() {
        return new QuizWidgetApplicationException(
                generateCode(2),
                "퀴즈 옵션을 찾을 수 없습니다.",
                Category.NOT_FOUND
        );
    }

    public static QuizWidgetApplicationException cannotUpdateQuizOption() {
        return new QuizWidgetApplicationException(
                generateCode(3),
                "이미 제출된 퀴즈는 수정할 수 없습니다.",
                Category.VALIDATION
        );
    }

    public static QuizWidgetApplicationException invalidQuizStatus() {
        return new QuizWidgetApplicationException(
                generateCode(4),
                "퀴즈가 진행 중인 경우에만 제출할 수 있습니다.",
                Category.VALIDATION
        );
    }

    public static QuizWidgetApplicationException alreadySubmitted() {
        return new QuizWidgetApplicationException(
                generateCode(5),
                "이미 제출하였습니다.",
                Category.VALIDATION
        );
    }

    private static String generateCode(int number) {
        return CODE_PREFIX + String.format("%03d", number);
    }
}
