package kr.composite.api.vote.infrastructure;

import java.util.List;
import kr.composite.api.vote.domain.VoteSubmission;
import org.springframework.data.jpa.repository.JpaRepository;

interface JpaVoteSubmissionRepository extends JpaRepository<VoteSubmission, Long> {

    List<VoteSubmission> findAllByVoteWidgetId(Long voteWidgetId);

    boolean existsByStudentIdAndVoteWidgetId(Long studentId, Long voteWidgetId);

    void deleteAllByVoteWidgetId(Long voteWidgetId);
}
