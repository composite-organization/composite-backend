package kr.composite.api.lesson.domain;

import java.util.Optional;

public interface LessonRepository {

    void save(Lesson lesson);

    Optional<Lesson> findByLessonCode(LessonCode lessonCode);

    Optional<Lesson> findById(Long id);
}
