package kr.composite.api.participant.infrastructure;

import kr.composite.api.participant.domain.Teacher;
import kr.composite.api.participant.domain.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaTeacherRepository implements TeacherRepository {

    private final SpringDataJpaTeacherRepository springDataJpaTeacherRepository;

    @Override
    public Teacher save(Teacher teacher) {
        return springDataJpaTeacherRepository.save(teacher);
    }

    @Override
    public Optional<Teacher> findByLessonIdAndUserId(Long lessonId, Long userId) {
        return springDataJpaTeacherRepository.findByLessonIdAndUserId(lessonId, userId);
    }

    @Override
    public Optional<Teacher> findByUserId(Long userId) {
        return springDataJpaTeacherRepository.findByUserId(userId);
    }

    @Override
    public Optional<Teacher> findByLessonId(Long lessonId) {
        return springDataJpaTeacherRepository.findByLessonId(lessonId);
    }
}
