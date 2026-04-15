package kr.composite.api.lesson.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.Test;

class LessonTest {

    @Test
    void 유효한_값으로_수업을_생성할_수_있다() {
        // given
        LessonName name = new LessonName("자바 프로그래밍");
        LessonCode code = new LessonCode("CODE123");
        LessonPassword password = new LessonPassword("pass123");

        // when
        Lesson lesson = new Lesson(name, code, password);

        // then
        assertAll(
                () -> assertThat(lesson.getName()).isEqualTo(name),
                () -> assertThat(lesson.getCode()).isEqualTo(code),
                () -> assertThat(lesson.getPassword()).isEqualTo(password)
        );
    }
}
