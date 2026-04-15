package kr.composite.api.vote.domain;

import java.util.Arrays;
import lombok.Getter;

@Getter
public enum VoteStatus {

    IN_PROGRESS("진행 중") {
        @Override
        public VoteStatus transitionTo(VoteStatus target) {
            if (target == ENDED) {
                return ENDED;
            }
            throw VoteDomainException.invalidStatusTransition(this, target);
        }
    },
    ENDED("종료") {
        @Override
        public VoteStatus transitionTo(VoteStatus target) {
            throw VoteDomainException.invalidStatusTransition(this, target);
        }
    };

    private final String description;

    VoteStatus(String description) {
        this.description = description;
    }

    public static VoteStatus from(String name) {
        if (name == null) {
            throw VoteDomainException.unsupportedStatus("null");
        }

        return Arrays.stream(values())
                .filter(value -> value.name().equals(name))
                .findFirst()
                .orElseThrow(() -> VoteDomainException.unsupportedStatus(name));
    }

    public static VoteStatus fromDescription(String description) {
        return Arrays.stream(values())
                .filter(value -> value.description.equals(description))
                .findFirst()
                .orElseThrow(() -> VoteDomainException.unsupportedStatus(description));
    }

    public abstract VoteStatus transitionTo(VoteStatus target);
}
