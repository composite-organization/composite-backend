package kr.composite.api.participant.infrastructure;

import kr.composite.api.participant.domain.Student;
import kr.composite.api.participant.domain.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

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
}
