package kr.composite.api.participant.domain;

public class ParticipatedStudent {

    private final Student student;
    private final Participant participant;

    public ParticipatedStudent(Student student, Participant participant) {
        this.student = student;
        this.participant = participant;
    }

    public Long studentId() {
        return student.getId();
    }

    public String participantName() {
        return participant.getName().getValue();
    }
}
