package kr.composite.api.student.infrastructure;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import kr.composite.api.student.domain.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataJpaStudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByLessonIdAndUserId(Long lessonId, Long userId);

    Optional<Student> findByUserId(Long userId);

    Boolean existsByLessonIdAndUserId(Long lessonId, Long userId);

    List<Student> findAllByIdIn(Collection<Long> ids);
}
