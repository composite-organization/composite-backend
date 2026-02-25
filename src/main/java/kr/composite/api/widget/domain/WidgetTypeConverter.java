package kr.composite.api.widget.domain;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class WidgetTypeConverter implements AttributeConverter<WidgetType, String> {

    @Override
    public String convertToDatabaseColumn(WidgetType widgetType) {
        if (widgetType == null) {
            return null;
        }

        return widgetType.getDescription();
    }

    @Override
    public WidgetType convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }

        return WidgetType.from(dbData);
    }
}
