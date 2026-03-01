package kr.composite.api.vote.domain;

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
public class VoteOption extends BaseEntity {

    @Column(name = "vote_widget_id")
    private Long voteWidgetId;

    @Column(name = "sequence")
    private Long sequence;

    @Column(name = "content")
    private String content;

}
