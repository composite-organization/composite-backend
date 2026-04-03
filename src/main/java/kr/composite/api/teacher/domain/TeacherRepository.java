package kr.composite.api.teacher.domain;

import java.util.Optional;

public interface TeacherRepository {

    Teacher save(Teacher teacher);

    Optional<Teacher> findByLessonIdAndUserId(Long lessonId, Long userId);

    Optional<Teacher> findByUserId(Long userId);

    Optional<Teacher> findByLessonId(Long lessonId);
}
