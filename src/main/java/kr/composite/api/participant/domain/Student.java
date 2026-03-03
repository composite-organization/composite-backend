package kr.composite.api.participant.domain;

import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Student extends Participant {

    public Student(Long lessonId, ParticipantName participantName) {
        super(lessonId, participantName);
    }
}
