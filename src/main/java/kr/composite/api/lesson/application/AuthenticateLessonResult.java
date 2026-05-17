package kr.composite.api.lesson.application;

public record AuthenticateLessonResult(
        String token,
        Long lessonId
) {

}
