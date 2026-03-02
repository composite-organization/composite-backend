package kr.composite.api.question.domain;

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
public class Question extends BaseEntity {

    @Column(name = "student_id")
    private Long studentId;

    @Column(name = "question_widget_id")
    private Long questionWidgetId;

    @Column(name = "content")
    private String content;

    @Column(name = "is_anonymous")
    private boolean isAnonymous;

    @Column(name = "is_completed")
    private boolean isCompleted;
}
