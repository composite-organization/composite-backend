package kr.composite.api.quiz.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import kr.composite.api.global.domain.BaseEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuizSubmission extends BaseEntity {

    @Column(name = "student_id")
    private Long studentId;

    @Column(name = "quiz_widget_id")
    private Long quizWidgetId;

    @Column(name = "quiz_option_id")
    private Long quizOptionId;
}
