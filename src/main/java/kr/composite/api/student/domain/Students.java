package kr.composite.api.student.domain;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Students {

    private final List<Student> students;

    public Students(List<Student> students) {
        this.students = List.copyOf(students);
    }

    public Map<Student, StudentName> nameByStudentId() {
        return students.stream()
                .collect(Collectors.toMap(student -> student, Student::getName));
    }
}
