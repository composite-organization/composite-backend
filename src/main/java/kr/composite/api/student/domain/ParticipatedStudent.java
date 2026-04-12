package kr.composite.api.student.domain;

public class ParticipatedStudent {

    private final Student student;

    public ParticipatedStudent(Student student) {
        this.student = student;
    }

    public Long studentId() {
        return student.getId();
    }

    public String studentName() {
        return student.getName().getValue();
    }
}
