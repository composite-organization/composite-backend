package kr.composite.api.quiz.domain;

import java.util.Arrays;
import lombok.Getter;

@Getter
public enum QuizStatus {

    NOT_STARTED("시작 전"),
    IN_PROGRESS("진행 중"),
    ENDED("종료");


    private String description;

    QuizStatus(String description) {
        this.description = description;
    }

    public static QuizStatus from(String description) {
        return Arrays.stream(values())
                .filter(status ->
                        status.description.equals(description)
                )
                .findFirst()
                .orElseThrow(IllegalArgumentException::new
                );
    }
}
