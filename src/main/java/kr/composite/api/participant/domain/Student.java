package kr.composite.api.participant.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import kr.composite.api.global.domain.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Student extends BaseEntity {

    @Column(name = "participant_id")
    private Long participantId;

    public Student(Long participantId) {
        this.participantId = participantId;
    }
}
