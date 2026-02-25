package kr.composite.api.attachment.domain;

import jakarta.persistence.Column;

public class AttachmentSize {

    @Column(name = "size")
    private final String value;

    public AttachmentSize(String value) {
        this.value = value;
    }
}
