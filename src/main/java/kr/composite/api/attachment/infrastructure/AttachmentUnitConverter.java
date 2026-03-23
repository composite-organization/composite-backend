package kr.composite.api.attachment.infrastructure;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import kr.composite.api.attachment.domain.AttachmentUnit;

@Converter
public class AttachmentUnitConverter implements AttributeConverter<AttachmentUnit, String> {

    @Override
    public String convertToDatabaseColumn(AttachmentUnit attachmentUnit) {
        if (attachmentUnit == null) {
            return null;
        }

        return attachmentUnit.getDescription();
    }

    @Override
    public AttachmentUnit convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }

        return AttachmentUnit.fromDescription(dbData);
    }
}
