package kr.composite.api.attachment.domain;

import jakarta.persistence.Column;
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
public class AttachmentFile extends BaseEntity {

    @Column(name = "attachment_widget_id")
    private Long attachmentWidgetId;

    @Column(name = "attachment_id")
    private Long attachmentId;
}
