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
public class QuizOption extends BaseEntity {

    @Column(name = "quiz_widget_id")
    private Long quizWidgetId;

    @Column(name = "content")
    private String content;

    @Column(name = "is_correct")
    private boolean isCorrect;
}
