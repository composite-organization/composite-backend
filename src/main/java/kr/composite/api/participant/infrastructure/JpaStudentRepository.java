package kr.composite.api.participant.infrastructure;

import java.util.List;
import java.util.Optional;
import kr.composite.api.participant.domain.Student;
import kr.composite.api.participant.domain.StudentRepository;
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
    public Optional<Student> findByParticipantId(Long participantId) {
        return springDataJpaStudentRepository.findByParticipantId(participantId);
    }

    @Override
    public List<Student> findAllByIdIn(List<Long> studentIds) {
        return springDataJpaStudentRepository.findAllByIdIn(studentIds);
    }
}
