package kr.composite.api.memo.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class MemoContent {

    private static final int MAX_LENGTH = 200;

    @Column(name = "content")
    private String value;

    public MemoContent(String value) {
        validateValue(value);
        this.value = value;
    }

    private void validateValue(String value) {
        if (value == null) {
            throw MemoDomainException.emptyContent();
        }
        if (value.length() > MAX_LENGTH) {
            throw MemoDomainException.invalidContentLength(MAX_LENGTH);
        }
    }
}
