package kr.composite.api.vote.infrastructure;

import java.util.List;
import kr.composite.api.vote.domain.VoteSubmission;
import kr.composite.api.vote.domain.VoteSubmissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class VoteSubmissionRepositoryAdapter implements VoteSubmissionRepository {

    private final JpaVoteSubmissionRepository jpaVoteSubmissionRepository;

    @Override
    public void saveAll(List<VoteSubmission> voteSubmissions) {
        jpaVoteSubmissionRepository.saveAll(voteSubmissions);
    }

    @Override
    public List<VoteSubmission> findAllByVoteWidgetId(Long voteWidgetId) {
        return jpaVoteSubmissionRepository.findAllByVoteWidgetId(voteWidgetId);
    }

    @Override
    public boolean existsByStudentIdAndVoteWidgetId(Long studentId, Long voteWidgetId) {
        return jpaVoteSubmissionRepository.existsByStudentIdAndVoteWidgetId(studentId, voteWidgetId);
    }

    @Override
    public void deleteAllByVoteWidgetId(Long voteWidgetId) {
        jpaVoteSubmissionRepository.deleteAllByVoteWidgetId(voteWidgetId);
    }
}
