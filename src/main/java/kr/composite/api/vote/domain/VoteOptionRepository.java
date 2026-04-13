package kr.composite.api.vote.domain;

import java.util.List;

public interface VoteOptionRepository {

    List<VoteOption> saveAll(List<VoteOption> voteOptions);

    List<VoteOption> findAllByVoteWidgetId(Long voteWidgetId);

    boolean existsAllByIdInAndVoteWidgetId(List<Long> ids, Long voteWidgetId);

    void deleteAllByVoteWidgetId(Long voteWidgetId);
}
