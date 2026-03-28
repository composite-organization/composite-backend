package kr.composite.api.participant.domain;

import java.util.Optional;

public interface StudentRepository {

    Student save(Student student);

    Optional<Student> findByParticipantId(Long participantId);
}
