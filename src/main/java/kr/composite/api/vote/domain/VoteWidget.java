package kr.composite.api.vote.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import kr.composite.api.global.domain.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VoteWidget extends BaseEntity {

    @Column(name = "widget_id")
    private Long widgetId;

    @Embedded
    private VoteTitle voteTitle;

    @Column(name = "is_anonymous")
    private boolean isAnonymous;

    @Column(name = "is_multi_selectable")
    private boolean isMultiSelectable;

    @Column(name = "status")
    private VoteStatus voteStatus;

    public VoteWidget(
            Long widgetId,
            VoteTitle voteTitle,
            boolean isAnonymous,
            boolean isMultiSelectable
    ) {
        this.widgetId = widgetId;
        this.voteTitle = voteTitle;
        this.isAnonymous = isAnonymous;
        this.isMultiSelectable = isMultiSelectable;
        this.voteStatus = VoteStatus.IN_PROGRESS;
    }

    public void changeStatus(VoteStatus target) {
        voteStatus = voteStatus.transitionTo(target);
    }
}
