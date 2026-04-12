package kr.composite.api.vote.infrastructure;

import java.util.Collection;
import java.util.List;
import kr.composite.api.vote.domain.VoteOption;
import kr.composite.api.vote.domain.VoteOptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class VoteOptionRepositoryAdapter implements VoteOptionRepository {

    private final JpaVoteOptionRepository jpaVoteOptionRepository;

    @Override
    public List<VoteOption> saveAll(List<VoteOption> voteOptions) {
        return jpaVoteOptionRepository.saveAll(voteOptions);
    }

    @Override
    public List<VoteOption> findAllByVoteWidgetId(Long voteWidgetId) {
        return jpaVoteOptionRepository.findAllByVoteWidgetId(voteWidgetId);
    }

    @Override
    public boolean existsAllByIdInAndVoteWidgetId(Collection<Long> ids, Long voteWidgetId) {
        return jpaVoteOptionRepository.countByIdInAndVoteWidgetId(ids, voteWidgetId) == ids.size();
    }

    @Override
    public void deleteAllByVoteWidgetId(Long voteWidgetId) {
        jpaVoteOptionRepository.deleteAllByVoteWidgetId(voteWidgetId);
    }
}
