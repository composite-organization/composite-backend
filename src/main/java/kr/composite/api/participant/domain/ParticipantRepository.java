package kr.composite.api.participant.domain;

import java.util.List;
import java.util.Optional;

public interface ParticipantRepository {

    Participant save(Participant participant);

    Optional<Participant> findByLessonIdAndUserId(Long lessonId, Long userId);

    Optional<Participant> findByUserId(Long userId);

    boolean existsByLessonIdAndUserId(Long lessonId, Long userId);

    List<Participant> findAllByIdIn(List<Long> participantIds);
}
