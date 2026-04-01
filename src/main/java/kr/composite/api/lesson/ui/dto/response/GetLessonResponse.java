package kr.composite.api.lesson.ui.dto.response;

import kr.composite.api.lesson.domain.Lesson;

public record GetLessonResponse(
        String lessonName
) {

    public static GetLessonResponse from(Lesson lesson) {
        return new GetLessonResponse(lesson.getName().getValue());
    }
}
