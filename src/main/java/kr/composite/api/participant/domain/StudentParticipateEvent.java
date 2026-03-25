package kr.composite.api.participant.domain;

import org.springframework.context.ApplicationEvent;

public class StudentParticipateEvent extends ApplicationEvent {

    private final Student student;
    private final Long lessonId;

    public StudentParticipateEvent(Student student, Long lessonId) {
        super(student);
        this.student = student;
        this.lessonId = lessonId;
    }

    public Student getStudent() {
        return student;
    }

    public Long getLessonId() {
        return lessonId;
    }
}
