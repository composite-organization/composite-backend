package kr.composite.api.student.domain;

import java.util.List;

public class Students {

    private final List<Student> students;

    public Students(List<Student> students) {
        this.students = List.copyOf(students);
    }

    public List<ParticipatedStudent> toParticipatedStudents() {
        return students.stream()
                .map(ParticipatedStudent::new)
                .toList();
    }
}
