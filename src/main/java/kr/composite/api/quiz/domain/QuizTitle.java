package kr.composite.api.quiz.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuizTitle {

    private static final int MIN_LENGTH = 1;
    private static final int MAX_LENGTH = 200;

    @Column(name = "title")
    private String value;

    public QuizTitle(String value) {
        validateValue(value);
        this.value = value;
    }

    private void validateValue(String value) {
        if (value == null) {
            throw QuizWidgetDomainException.emptyTitle();
        }
        if (value.length() < MIN_LENGTH || value.length() > MAX_LENGTH) {
            throw QuizWidgetDomainException.invalidTitleLength(value, MIN_LENGTH, MAX_LENGTH);
        }
    }
}
