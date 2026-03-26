package kr.composite.api.attachment.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AttachmentSize {

    @Column(name = "size")
    private Long value;

    public AttachmentSize(Long value) {
        this.value = value;
    }

    public double getFormattedSize(Long threshold) {
        return (double) value / threshold;
    }
}
