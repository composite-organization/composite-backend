package kr.composite.api.vote.domain;

import java.util.List;

public interface VoteSubmissionRepository {

    void saveAll(List<VoteSubmission> voteSubmissions);

    List<VoteSubmission> findAllByVoteWidgetId(Long voteWidgetId);

    boolean existsByStudentIdAndVoteWidgetId(Long studentId, Long voteWidgetId);

    void deleteAllByVoteWidgetId(Long voteWidgetId);
}
