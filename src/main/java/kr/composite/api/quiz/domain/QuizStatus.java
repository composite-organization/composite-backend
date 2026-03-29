package kr.composite.api.quiz.domain;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum QuizStatus {

    NOT_STARTED("시작 전"),
    IN_PROGRESS("진행 중"),
    ENDED("종료");

    private final String description;

    QuizStatus(String description) {
        this.description = description;
    }

    public static QuizStatus fromDescription(String description) {
        return Arrays.stream(values())
                .filter(value -> value.description.equals(description))
                .findFirst()
                .orElseThrow(() -> QuizDomainException.unsupportedStatus(description));
    }
}
