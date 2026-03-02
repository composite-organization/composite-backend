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
public class QuestionWidget extends BaseEntity {

    @Column(name = "widget_id")
    private Long widgetId;
}
