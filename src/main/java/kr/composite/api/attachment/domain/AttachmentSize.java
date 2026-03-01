package kr.composite.api.attachment.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AttachmentSize {

    @Column(name = "size")
    private String value;

    public AttachmentSize(String value) {
        this.value = value;
    }
}
