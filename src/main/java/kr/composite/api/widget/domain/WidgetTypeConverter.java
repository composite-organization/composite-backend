package kr.composite.api.widget.domain;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class WidgetTypeConverter implements AttributeConverter<WidgetType, String> {

    @Override
    public String convertToDatabaseColumn(WidgetType attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getDescription();
    }

    @Override
    public WidgetType convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        return WidgetType.from(dbData);
    }
}
