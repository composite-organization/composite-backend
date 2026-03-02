package kr.composite.api.widget.domain;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;

public enum WidgetType {

    MEMO("MEMO"),
    ATTACHMENT("ATTACHMENT"),
    QUIZ("QUIZ"),
    VOTE("VOTE");

    private final String description;

    WidgetType(String description) {
        this.description = description;
    }

    @Converter
    public class WidgetTypeConverter implements AttributeConverter<WidgetType, String> {

        @Override
        public String convertToDatabaseColumn(WidgetType widgetType) {
            if (widgetType == null) {
                return null;
            }

            return widgetType.description;
        }

        @Override
        public WidgetType convertToEntityAttribute(String dbData) {
            if (dbData == null) {
                return null;
            }

            return Arrays.stream(values())
                    .filter(value -> value.description.equals(description))
                    .findFirst()
                    .orElseThrow(IllegalArgumentException::new);
        }
    }
}
