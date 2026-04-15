package kr.composite.api.vote.infrastructure;

import java.util.Collection;
import java.util.List;
import kr.composite.api.vote.domain.VoteOption;
import org.springframework.data.jpa.repository.JpaRepository;

interface JpaVoteOptionRepository extends JpaRepository<VoteOption, Long> {

    List<VoteOption> findAllByVoteWidgetId(Long voteWidgetId);

    long countByIdInAndVoteWidgetId(Collection<Long> ids, Long voteWidgetId);

    void deleteAllByVoteWidgetId(Long voteWidgetId);
}
