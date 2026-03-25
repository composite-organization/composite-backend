package kr.composite.api.widget.infrastructure;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import kr.composite.api.widget.domain.WidgetType;

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

        return WidgetType.fromDescription(dbData);
    }
}
