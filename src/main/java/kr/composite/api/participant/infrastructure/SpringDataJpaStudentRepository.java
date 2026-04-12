package kr.composite.api.participant.infrastructure;

import java.util.List;
import java.util.Optional;
import kr.composite.api.participant.domain.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataJpaStudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByParticipantId(Long participantId);

    List<Student> findAllByIdIn(List<Long> studentIds);
}
