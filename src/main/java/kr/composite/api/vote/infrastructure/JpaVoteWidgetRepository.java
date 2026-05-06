package kr.composite.api.vote.infrastructure;

import java.util.Optional;
import kr.composite.api.vote.domain.VoteWidget;
import org.springframework.data.jpa.repository.JpaRepository;

interface JpaVoteWidgetRepository extends JpaRepository<VoteWidget, Long> {

    Optional<VoteWidget> findByWidgetId(Long widgetId);
}
