package kr.composite.api.participant.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import kr.composite.api.student.domain.Student;
import kr.composite.api.student.domain.StudentName;
import org.junit.jupiter.api.Test;

class StudentTest {

    @Test
    void 유효한_값으로_학생을_생성할_수_있다() {
        // given
        Long userId = 1L;
        Long lessonId = 1L;
        StudentName name = new StudentName("홍길동");

        // when
        Student student = new Student(userId, lessonId, name);

        // then
        assertAll(
                () -> assertThat(student.getUserId()).isEqualTo(userId),
                () -> assertThat(student.getLessonId()).isEqualTo(lessonId),
                () -> assertThat(student.getName()).isEqualTo(name)
        );
    }
}
