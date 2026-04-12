package kr.composite.api.vote.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VoteOptionContent {

    private static final int MIN_LENGTH = 1;
    private static final int MAX_LENGTH = 100;

    @Column(name = "content")
    private String value;

    public VoteOptionContent(String value) {
        validateValue(value);
        this.value = value;
    }

    private void validateValue(String value) {
        if (value == null) {
            throw VoteDomainException.emptyOptionContent();
        }
        if (value.length() < MIN_LENGTH || value.length() > MAX_LENGTH) {
            throw VoteDomainException.invalidOptionContentLength(value, MIN_LENGTH, MAX_LENGTH);
        }
    }
}
