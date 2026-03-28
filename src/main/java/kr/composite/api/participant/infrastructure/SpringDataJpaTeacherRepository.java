package kr.composite.api.participant.infrastructure;

import kr.composite.api.participant.domain.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringDataJpaTeacherRepository extends JpaRepository<Teacher, Long> {

    Optional<Teacher> findByParticipantId(Long participantId);
}
