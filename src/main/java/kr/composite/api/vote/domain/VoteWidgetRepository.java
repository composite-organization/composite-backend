package kr.composite.api.vote.domain;

import java.util.Optional;

public interface VoteWidgetRepository {

    VoteWidget save(VoteWidget voteWidget);

    Optional<VoteWidget> findById(Long voteWidgetId);

    Optional<VoteWidget> findByWidgetId(Long widgetId);

    void deleteById(Long voteWidgetId);
}
