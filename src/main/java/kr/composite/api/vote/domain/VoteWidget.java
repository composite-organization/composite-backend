package kr.composite.api.vote.domain;

import jakarta.persistence.Column;
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
public class VoteWidget extends BaseEntity {

    @Column(name = "widget_id")
    private Long widgetId;

    @Embedded
    private VoteTitle voteTitle;

    @Embedded
    private VoteDescription voteDescription;

    @Column(name = "is_anonymous")
    private boolean isAnonymous;

    @Column(name = "is_multi_selectable")
    private boolean isMultiSelectable;
}
