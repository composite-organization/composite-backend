package kr.composite.api.vote.infrastructure;

import java.util.Optional;
import kr.composite.api.vote.domain.VoteWidget;
import kr.composite.api.vote.domain.VoteWidgetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class VoteWidgetRepositoryAdapter implements VoteWidgetRepository {

    private final JpaVoteWidgetRepository jpaVoteWidgetRepository;

    @Override
    public VoteWidget save(VoteWidget voteWidget) {
        return jpaVoteWidgetRepository.save(voteWidget);
    }

    @Override
    public Optional<VoteWidget> findById(Long voteWidgetId) {
        return jpaVoteWidgetRepository.findById(voteWidgetId);
    }

    @Override
    public Optional<VoteWidget> findByWidgetId(Long widgetId) {
        return jpaVoteWidgetRepository.findByWidgetId(widgetId);
    }

    @Override
    public void deleteById(Long voteWidgetId) {
        jpaVoteWidgetRepository.deleteById(voteWidgetId);
    }
}
