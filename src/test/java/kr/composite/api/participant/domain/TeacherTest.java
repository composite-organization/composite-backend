package kr.composite.api.participant.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.Test;

class TeacherTest {

    @Test
    void 유효한_값으로_선생님을_생성할_수_있다() {
        // given
        Long userId = 1L;
        Long lessonId = 1L;
        TeacherName name = new TeacherName("김선생");

        // when
        Teacher teacher = new Teacher(userId, lessonId, name);

        // then
        assertAll(
                () -> assertThat(teacher.getUserId()).isEqualTo(userId),
                () -> assertThat(teacher.getLessonId()).isEqualTo(lessonId),
                () -> assertThat(teacher.getName()).isEqualTo(name)
        );
    }
}
