package kr.composite.api.student.domain;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class Students {

    private final Map<Long, Student> studentById;

    public Students(List<Student> students) {
        this.studentById = students.stream()
                .collect(Collectors.toUnmodifiableMap(Student::getId, student -> student));
    }

    public Optional<StudentName> findName(Long studentId) {
        return Optional.ofNullable(studentById.get(studentId))
                .map(Student::getName);
    }
}
