package kr.composite.api.participant.infrastructure;

import java.util.Optional;
import kr.composite.api.participant.domain.Participant;
import kr.composite.api.participant.domain.ParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JpaParticipantRepository implements ParticipantRepository {

    private final SpringDataJpaParticipantRepository springDataJpaParticipantRepository;

    @Override
    public Participant save(Participant participant) {
        return springDataJpaParticipantRepository.save(participant);
    }

    @Override
    public Optional<Participant> findByLessonIdAndUserId(Long lessonId, Long userId) {
        return springDataJpaParticipantRepository.findByLessonIdAndUserId(lessonId, userId);
    }

    @Override
    public Optional<Participant> findByUserId(Long userId) {
        return springDataJpaParticipantRepository.findByUserId(userId);
    }

    @Override
    public boolean existsByLessonIdAndUserId(Long lessonId, Long userId) {
        return springDataJpaParticipantRepository.existsByLessonIdAndUserId(lessonId, userId);
    }
}
