package kr.composite.api.vote.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VoteTitle {

    private static final int MIN_LENGTH = 1;
    private static final int MAX_LENGTH = 200;

    @Column(name = "title")
    private String value;

    public VoteTitle(String value) {
        validateValue(value);
        this.value = value;
    }

    private void validateValue(String value) {
        if (value == null) {
            throw VoteDomainException.emptyTitle();
        }
        if (value.length() < MIN_LENGTH || value.length() > MAX_LENGTH) {
            throw VoteDomainException.invalidTitleLength(value, MIN_LENGTH, MAX_LENGTH);
        }
    }
}
