package kr.composite.api.attachment.domain;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;

public enum AttachmentUnit {
    KB("KB"),
    MB("MB");

    private final String description;

    AttachmentUnit(String description) {
        this.description = description;
    }

    @Converter
    static class AttachmentUnitConverter implements AttributeConverter<AttachmentUnit, String> {

        @Override
        public String convertToDatabaseColumn(AttachmentUnit attachmentUnit) {
            if (attachmentUnit == null) {
                return null;
            }

            return attachmentUnit.description;
        }

        @Override
        public AttachmentUnit convertToEntityAttribute(String dbData) {
            if (dbData == null) {
                return null;
            }

            return Arrays.stream(values())
                    .filter(value -> value.description.equals(dbData))
                    .findFirst()
                    .orElseThrow(IllegalArgumentException::new);
        }
    }
}
