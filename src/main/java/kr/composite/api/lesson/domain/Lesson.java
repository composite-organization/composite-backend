package kr.composite.api.lesson.domain;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import kr.composite.api.global.domain.BaseEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Lesson extends BaseEntity {

    @Embedded
    private LessonName name;

    @Embedded
    private LessonCode code;

    @Embedded
    private LessonPassword password;
}
