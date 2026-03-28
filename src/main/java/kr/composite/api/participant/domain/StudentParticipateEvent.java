package kr.composite.api.participant.domain;

public record StudentParticipateEvent(
        Student student,
        Long lessonId
) {
}
