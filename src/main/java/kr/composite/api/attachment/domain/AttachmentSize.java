package kr.composite.api.attachment.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AttachmentSize {

    @Column(name = "size")
    private int value;

    public AttachmentSize(int value) {
        this.value = value;
    }
}
