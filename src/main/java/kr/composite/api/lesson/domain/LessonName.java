package kr.composite.api.lesson.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class LessonName {

    private static final int MIN_LENGTH = 1;
    private static final int MAX_LENGTH = 20;

    @Column(name = "name")
    private String value;

    public LessonName(String value) {
        validateValue(value);
        this.value = value;
    }

    private void validateValue(String value) {
        if (value == null) {
            throw LessonDomainException.emptyName();
        }
        if (value.length() < MIN_LENGTH || value.length() > MAX_LENGTH) {
            throw LessonDomainException.invalidNameLength(value, MIN_LENGTH, MAX_LENGTH);
        }
    }
}
