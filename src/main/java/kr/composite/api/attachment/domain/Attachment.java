package kr.composite.api.attachment.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import kr.composite.api.global.domain.BaseEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Attachment extends BaseEntity {

    @Column(name = "attachment_widget_id")
    private Long attachmentWidgetId;

    @Column(name = "url")
    private String url;

    @Embedded
    private AttachmentName attachmentName;

    @Embedded
    private AttachmentSize attachmentSize;

    @Column(name = "unit")
    @Convert(converter = AttachmentUnit.Converter.class)
    private AttachmentUnit unit;
}
