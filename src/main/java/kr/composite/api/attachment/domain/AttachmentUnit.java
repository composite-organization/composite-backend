package kr.composite.api.attachment.domain;

import java.util.Arrays;
import lombok.Getter;

@Getter
public enum AttachmentUnit {

    MB("MB", 1024L * 1024L),
    KB("KB", 1024L);

    private final String description;
    private final Long byteSize;

    AttachmentUnit(String description, Long byteSize) {
        this.description = description;
        this.byteSize = byteSize;
    }

    public static AttachmentUnit fromDescription(String description) {
        return Arrays.stream(values())
                .filter(value -> value.description.equals(description))
                .findFirst()
                .orElseThrow(() -> AttachmentDomainException.invalidUnitDescription(description));
    }

    public static AttachmentUnit getAppropriateUnit(Long byteSize) {
        return Arrays.stream(AttachmentUnit.values())
                .filter(unit -> byteSize >= unit.byteSize)
                .findFirst()
                .orElse(KB);
    }
}
