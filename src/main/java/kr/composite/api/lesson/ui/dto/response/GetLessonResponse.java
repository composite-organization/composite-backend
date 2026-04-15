package kr.composite.api.lesson.ui.dto.response;

public record GetLessonResponse(
        String lessonName,
        String teacherName
) {

    public static GetLessonResponse from(String lessonName, String teacherName) {
        return new GetLessonResponse(lessonName, teacherName);
    }
}
