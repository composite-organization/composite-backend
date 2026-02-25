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
public class VoteSubmission extends BaseEntity {

    @Column(name = "student_id")
    private Long studentId;

    @Column(name = "vote_widget_id")
    private Long voteWidgetId;

    @Column(name = "vote_option_id")
    private Long voteOptionId;
}
