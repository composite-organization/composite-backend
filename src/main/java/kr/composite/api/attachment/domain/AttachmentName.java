package kr.composite.api.attachment.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AttachmentName {

    private static final int MIN_LENGTH = 1;

    @Column(name = "name")
    private String value;

    public AttachmentName(String value) {
        validateValue(value);
        this.value = value;
    }

    private void validateValue(String value) {
        if (value == null || value.isEmpty()) {
            throw AttachmentDomainException.emptyName();
        }
        if (value.length() < MIN_LENGTH) {
            throw AttachmentDomainException.invalidNameLength(value, MIN_LENGTH);
        }
    }
}
