package kr.composite.api.participant.domain;

import java.util.List;
import java.util.Optional;

public interface StudentRepository {

    Student save(Student student);

    Optional<Student> findByParticipantId(Long participantId);

    List<Student> findAllByIdIn(List<Long> studentIds);
}
