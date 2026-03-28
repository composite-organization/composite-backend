package kr.composite.api.participant.infrastructure;

import java.util.Optional;
import kr.composite.api.participant.domain.Participant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataJpaParticipantRepository extends JpaRepository<Participant, Long> {

    Optional<Participant> findByLessonIdAndUserId(Long lessonId, Long userId);

    Optional<Participant> findByUserId(Long userId);

    boolean existsByLessonIdAndUserId(Long lessonId, Long userId);
}
