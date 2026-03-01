package kr.composite.api.widget.domain;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum WidgetType {

    MEMO("MEMO"),
    ATTACHMENT("ATTACHMENT"),
    QUIZ("QUIZ"),
    VOTE("VOTE");

    private final String description;

    WidgetType(String description) {
        this.description = description;
    }

    public static WidgetType from(String description) {
        return Arrays.stream(values())
                .filter(value -> value.description.equals(description))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
