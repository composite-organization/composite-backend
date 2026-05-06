package kr.composite.api.vote.domain;

import java.util.List;
import java.util.Optional;

public interface VoteWidgetRepository {

    VoteWidget save(VoteWidget voteWidget);

    Optional<VoteWidget> findById(Long voteWidgetId);

    List<VoteWidget> findAllByWidgetIdIn(List<Long> widgetIds);

    void deleteById(Long voteWidgetId);
}
