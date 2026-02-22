package kr.composite.api.widget.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
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
public class Widget extends BaseEntity {

    @Column(name = "lesson_id")
    private Long lessonId;

    @Column(name = "type")
    @Convert(converter = WidgetTypeConverter.class)
    private WidgetType widgetType;
}
