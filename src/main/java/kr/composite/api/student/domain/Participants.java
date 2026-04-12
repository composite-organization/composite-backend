package kr.composite.api.student.domain;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Participants {

    private final List<ParticipatedStudent> participatedStudents;

    public Participants(List<ParticipatedStudent> participatedStudents) {
        this.participatedStudents = List.copyOf(participatedStudents);
    }

    public Map<Long, String> nameByStudentId() {
        return participatedStudents.stream()
                .collect(Collectors.toMap(
                        ParticipatedStudent::studentId,
                        ParticipatedStudent::studentName
                ));
    }
}
