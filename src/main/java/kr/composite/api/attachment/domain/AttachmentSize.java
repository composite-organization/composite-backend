package kr.composite.api.attachment.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AttachmentSize {

    private static final long KB_THRESHOLD = 1024L;
    private static final long MB_THRESHOLD = 1024L * 1024L;

    @Column(name = "size")
    private Long value;

    public AttachmentSize(Long value) {
        this.value = value;
    }

    // 1MB 이상이면 MB, 미만이면 KB
    public AttachmentUnit getAppropriateUnit() {
        if (this.value >= MB_THRESHOLD) {
            return AttachmentUnit.MB;
        }
        return AttachmentUnit.KB;
    }

    public double getFormattedSize() {
        AttachmentUnit unit = getAppropriateUnit();
        if (unit == AttachmentUnit.MB) {
            return (double) value / MB_THRESHOLD;
        }
        return (double) value / KB_THRESHOLD;
    }
}
