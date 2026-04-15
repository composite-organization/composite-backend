package kr.composite.api.vote.infrastructure;

import kr.composite.api.vote.domain.VoteWidget;
import org.springframework.data.jpa.repository.JpaRepository;

interface JpaVoteWidgetRepository extends JpaRepository<VoteWidget, Long> {

}
