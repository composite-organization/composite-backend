package kr.composite.api.participant.domain;

import java.util.Optional;

public interface StudentRepository {

    Student save(Student student);

    Optional<Student> findByLessonIdAndUserId(Long lessonId, Long userId);

    Optional<Student> findByUserId(Long userId);
}
