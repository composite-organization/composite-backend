package kr.composite.api.quiz.domain;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class QuizStatusConverter implements AttributeConverter<QuizStatus, String> {

    @Override
    public String convertToDatabaseColumn(QuizStatus quizStatus) {
        if (quizStatus == null) {
            return null;
        }

        return quizStatus.getDescription();
    }

    @Override
    public QuizStatus convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }

        return QuizStatus.from(dbData);
    }
}
