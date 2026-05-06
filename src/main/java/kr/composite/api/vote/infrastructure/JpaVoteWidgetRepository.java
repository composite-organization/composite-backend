package kr.composite.api.vote.infrastructure;

import java.util.List;
import java.util.Optional;
import kr.composite.api.vote.domain.VoteWidget;
import org.springframework.data.jpa.repository.JpaRepository;

interface JpaVoteWidgetRepository extends JpaRepository<VoteWidget, Long> {

    List<VoteWidget> findAllByWidgetIdIn(List<Long> widgetIds);
}
