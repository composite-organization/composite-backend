package kr.composite.api.quiz.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import kr.composite.api.global.domain.BaseEntity;
import kr.composite.api.quiz.infrastructure.QuizStatusConverter;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuizWidget extends BaseEntity {

    @Column(name = "widget_id")
    private Long widgetId;

    @Embedded
    private QuizTitle title;

    @Column(name = "status")
    @Convert(converter = QuizStatusConverter.class)
    private QuizStatus quizStatus;
}
