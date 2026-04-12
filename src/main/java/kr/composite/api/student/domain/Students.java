package kr.composite.api.student.domain;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Students {

    private final List<Student> students;

    public Students(List<Student> students) {
        this.students = List.copyOf(students);
    }

    public Map<Long, String> nameByStudentId() {
        return students.stream()
                .collect(Collectors.toMap(Student::getId, student -> student.getName().getValue()));
    }
}
