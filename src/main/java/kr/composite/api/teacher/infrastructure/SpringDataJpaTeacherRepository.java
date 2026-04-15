package kr.composite.api.teacher.infrastructure;

import kr.composite.api.teacher.domain.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringDataJpaTeacherRepository extends JpaRepository<Teacher, Long> {

    Optional<Teacher> findByLessonIdAndUserId(Long lessonId, Long userId);

    Optional<Teacher> findByUserId(Long userId);

    Optional<Teacher> findByLessonId(Long lessonId);

    Boolean existsByLessonIdAndUserId(Long lessonId, Long userId);
}
