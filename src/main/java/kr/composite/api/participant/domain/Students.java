package kr.composite.api.participant.domain;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Students {

    private final List<Student> students;

    public Students(List<Student> students) {
        this.students = List.copyOf(students);
    }

    public List<Long> participantIds() {
        return students.stream()
                .map(Student::getParticipantId)
                .toList();
    }

    public List<ParticipatedStudent> pairWithParticipants(Participants participants) {
        Map<Long, Participant> participantIndex = participants.indexById();

        return students.stream()
                .map(student -> new ParticipatedStudent(
                        student,
                        Optional.ofNullable(participantIndex.get(student.getParticipantId()))
                                .orElseThrow(ParticipantDomainException::participantNotFound)
                ))
                .toList();
    }

}
