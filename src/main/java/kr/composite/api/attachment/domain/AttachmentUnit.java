package kr.composite.api.attachment.domain;

import java.util.Arrays;
import lombok.Getter;

@Getter
public enum AttachmentUnit {

    KB("KB"),
    MB("MB");

    private final String description;

    AttachmentUnit(String description) {
        this.description = description;
    }

    public static AttachmentUnit fromDescription(String description) {
        return Arrays.stream(values())
                .filter(value -> value.description.equals(description))
                .findFirst()
                .orElseThrow(() -> AttachmentDomainException.invalidUnitDescription(description));
    }
}
