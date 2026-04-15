package kr.composite.api.student.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StudentName {

    private static final int MIN_LENGTH = 1;
    private static final int MAX_LENGTH = 50;

    @Column(name = "name")
    private String value;

    public StudentName(String value) {
        validateValue(value);
        this.value = value;
    }

    private void validateValue(String value) {
        if (value == null || value.isBlank()) {
            throw StudentDomainException.emptyName();
        }
        if (value.length() < MIN_LENGTH || value.length() > MAX_LENGTH) {
            throw StudentDomainException.invalidNameLength(value, MIN_LENGTH, MAX_LENGTH);
        }
    }
}
