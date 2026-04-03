package kr.composite.api.lesson.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.regex.Pattern;
import lombok.NoArgsConstructor;

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
            throw LessonDomainException.emptyPassword();
        }
        if (value.length() < MIN_LENGTH || value.length() > MAX_LENGTH) {
            throw LessonDomainException.invalidPasswordLength(MIN_LENGTH, MAX_LENGTH);
        }
        if (!ALPHABET_NUMERIC_PATTERN.matcher(value).matches()) {
            throw LessonDomainException.invalidPasswordPattern();
        }
    }

    public void verify(String password) {
        if (value.equals(password)) {
            return;
        }
        throw LessonDomainException.invalidPassword();
    }
}
