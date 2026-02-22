package kr.composite.api.lesson.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.NoArgsConstructor;

import java.util.regex.Pattern;

@Embeddable
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class LessonPassword {

    private static final int MIN_LENGTH = 1;
    private static final int MAX_LENGTH = 20;
    private static final Pattern ALPHABET_NUMERIC_PATTERN = Pattern.compile("^[a-zA-Z0-9]+$");

    @Column(name = "password")
    private String value;

    public LessonPassword(String value) {
        validateValue(value);
        this.value = value;
    }

    private void validateValue(String value) {
        if (value == null) {
            throw new IllegalArgumentException();
        }
        if (value.length() < MIN_LENGTH || value.length() > MAX_LENGTH) {
            throw new IllegalArgumentException();
        }
        if (!ALPHABET_NUMERIC_PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException();
        }
    }
}
