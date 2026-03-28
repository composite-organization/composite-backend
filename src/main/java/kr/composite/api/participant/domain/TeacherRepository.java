package kr.composite.api.participant.domain;

import java.util.Optional;

public interface TeacherRepository {

    Teacher save(Teacher teacher);

    Optional<Teacher> findByParticipantId(Long participantId);
}
