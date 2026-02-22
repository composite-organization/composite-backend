package kr.composite.api.lesson.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class LessonCode {

    @Column(name = "code")
    private String value;

    public LessonCode(String value) {
        validateValue(value);
        this.value = value;
    }

    private void validateValue(String value) {
        if (value == null) {
            throw new IllegalArgumentException();
        }
        if (value.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }
}
