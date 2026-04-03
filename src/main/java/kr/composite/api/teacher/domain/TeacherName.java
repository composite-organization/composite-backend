package kr.composite.api.teacher.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class TeacherName {

    private static final int MIN_LENGTH = 1;
    private static final int MAX_LENGTH = 50;

    @Column(name = "name")
    private String value;

    public TeacherName(String value) {
        validateValue(value);
        this.value = value;
    }

    private void validateValue(String value) {
        if (value == null || value.isBlank()) {
            throw TeacherDomainException.emptyName();
        }
        if (value.length() < MIN_LENGTH || value.length() > MAX_LENGTH) {
            throw TeacherDomainException.invalidNameLength(value, MIN_LENGTH, MAX_LENGTH);
        }
    }
}
