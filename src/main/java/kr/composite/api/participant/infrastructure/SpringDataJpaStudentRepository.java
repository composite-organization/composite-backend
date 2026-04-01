package kr.composite.api.participant.infrastructure;

import kr.composite.api.participant.domain.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringDataJpaStudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByLessonIdAndUserId(Long lessonId, Long userId);

    Optional<Student> findByUserId(Long userId);
}
