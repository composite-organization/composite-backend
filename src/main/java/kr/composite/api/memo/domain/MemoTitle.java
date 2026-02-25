package kr.composite.api.memo.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class MemoTitle {

    private static final int MIN_LENGTH = 1;
    private static final int MAX_LENGTH = 50;

    @Column(name = "title")
    private String value;

    public MemoTitle(String value) {
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
    }
}
