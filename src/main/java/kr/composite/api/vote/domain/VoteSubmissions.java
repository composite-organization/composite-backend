package kr.composite.api.vote.domain;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class VoteSubmissions {

    private final Map<VoteOption, List<VoteSubmission>> submissions;

    public VoteSubmissions(List<VoteOption> voteOptions, List<VoteSubmission> submissions) {
        Map<Long, List<VoteSubmission>> submissionsByOptionId = submissions.stream()
                .collect(Collectors.groupingBy(VoteSubmission::getVoteOptionId));

        this.submissions = Collections.unmodifiableMap(
                voteOptions.stream()
                        .collect(Collectors.toMap(
                                option -> option,
                                option -> List.copyOf(submissionsByOptionId.getOrDefault(option.getId(), List.of())),
                                (a, b) -> a,
                                LinkedHashMap::new
                        ))
        );
    }

    public List<VoteOption> getVoteOptions() {
        return List.copyOf(submissions.keySet());
    }

    public long countDistinctStudents() {
        return getDistinctStudentIds().size();
    }

    public List<Long> getDistinctStudentIds() {
        return submissions.values().stream()
                .flatMap(List::stream)
                .map(VoteSubmission::getStudentId)
                .distinct()
                .toList();
    }

    public long maxSubmissionCount() {
        return submissions.values().stream()
                .mapToLong(List::size)
                .max()
                .orElse(0L);
    }

    public long countSubmissions(VoteOption option) {
        return getSubmissions(option).size();
    }

    public List<VoteSubmission> getSubmissions(VoteOption option) {
        return submissions.getOrDefault(option, List.of());
    }
}
