package kr.composite.api.quiz.domain;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;

public enum QuizStatus {

    NOT_STARTED("시작 전"),
    IN_PROGRESS("진행 중"),
    ENDED("종료");

    private final String description;

    QuizStatus(String description) {
        this.description = description;
    }

    @Converter
    public static class QuizStatusConverter implements AttributeConverter<QuizStatus, String> {

        @Override
        public String convertToDatabaseColumn(QuizStatus quizStatus) {
            if (quizStatus == null) {
                return null;
            }

            return quizStatus.description;
        }

        @Override
        public QuizStatus convertToEntityAttribute(String dbData) {
            if (dbData == null) {
                return null;
            }

            return Arrays.stream(values())
                    .filter(value -> value.description.equals(dbData))
                    .findFirst()
                    .orElseThrow(IllegalArgumentException::new);
        }
    }
}
