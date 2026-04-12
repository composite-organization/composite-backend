package kr.composite.api.student.infrastructure;

import java.util.Optional;
import kr.composite.api.student.domain.Student;
import kr.composite.api.student.domain.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JpaStudentRepository implements StudentRepository {

    private final SpringDataJpaStudentRepository springDataJpaStudentRepository;

    @Override
    public Student save(Student student) {
        return springDataJpaStudentRepository.save(student);
    }

    @Override
    public Optional<Student> findByLessonIdAndUserId(Long lessonId, Long userId) {
        return springDataJpaStudentRepository.findByLessonIdAndUserId(lessonId, userId);
    }

    @Override
    public Optional<Student> findByUserId(Long userId) {
        return springDataJpaStudentRepository.findByUserId(userId);
    }

    @Override
    public Boolean existsByLessonIdAndUserId(Long lessonId, Long userId) {
        return springDataJpaStudentRepository.existsByLessonIdAndUserId(lessonId, userId);
    }
}
