package kr.composite.api.student.domain;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface StudentRepository {

    Student save(Student student);

    Optional<Student> findByLessonIdAndUserId(Long lessonId, Long userId);

    Optional<Student> findByUserId(Long userId);

    Boolean existsByLessonIdAndUserId(Long lessonId, Long userId);

    List<Student> findAllByIdIn(Collection<Long> ids);
}
