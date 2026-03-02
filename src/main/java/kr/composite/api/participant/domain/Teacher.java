package kr.composite.api.participant.domain;

import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Teacher extends Participant {

    public Teacher(Long lessonId) {
        super(lessonId);
    }
}
