package kr.composite.api.lesson.infrastructure;

import java.util.Optional;
import kr.composite.api.lesson.domain.Lesson;
import kr.composite.api.lesson.domain.LessonCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataJpaLessonRepository extends JpaRepository<Lesson, Long> {

    Optional<Lesson> findLessonByCode(LessonCode code);
}
