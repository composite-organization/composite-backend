package kr.composite.api.vote.domain;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class VoteSubmissions {

    private final List<VoteSubmission> submissions;

    public VoteSubmissions(List<VoteSubmission> submissions) {
        this.submissions = List.copyOf(submissions);
    }

    public long countParticipants() {
        return submissions.stream()
                .map(VoteSubmission::getStudentId)
                .distinct()
                .count();
    }

    public List<Long> getDistinctStudentIds() {
        return submissions.stream()
                .map(VoteSubmission::getStudentId)
                .distinct()
                .toList();
    }

    public Map<VoteOption, Long> countByOptions(List<VoteOption> voteOptions) {
        Map<Long, VoteOption> optionIndex = indexOptionsById(voteOptions);

        return submissions.stream()
                .collect(Collectors.groupingBy(
                        submission -> optionIndex.get(submission.getVoteOptionId()),
                        Collectors.counting()
                ));
    }

    public Map<VoteOption, List<VoteSubmission>> groupByOptions(List<VoteOption> voteOptions) {
        Map<Long, VoteOption> optionIndex = indexOptionsById(voteOptions);

        return submissions.stream()
                .collect(Collectors.groupingBy(
                        submission -> optionIndex.get(submission.getVoteOptionId())
                ));
    }

    private Map<Long, VoteOption> indexOptionsById(List<VoteOption> voteOptions) {
        return voteOptions.stream()
                .collect(Collectors.toMap(VoteOption::getId, option -> option));
    }
}
