package kr.composite.api.participant.domain;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import kr.composite.api.global.domain.BaseEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@MappedSuperclass
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Participant extends BaseEntity {

    @Column(name = "lesson_id")
    protected Long lessonId;
}

