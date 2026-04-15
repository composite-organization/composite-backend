package kr.composite.api.student.domain;

public record StudentParticipateEvent(
        Student student,
        Long lessonId
) {
}
