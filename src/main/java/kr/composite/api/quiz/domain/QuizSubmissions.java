package kr.composite.api.quiz.domain;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class QuizSubmissions {

    private final Map<QuizOption, List<QuizSubmission>> submissions;

    public QuizSubmissions(List<QuizOption> quizOptions, List<QuizSubmission> submissions) {
        Map<Long, List<QuizSubmission>> submissionsByOptionId = submissions.stream()
                .collect(Collectors.groupingBy(QuizSubmission::getQuizOptionId));

        this.submissions = Collections.unmodifiableMap(
                quizOptions.stream()
                        .collect(Collectors.toMap(
                                option -> option,
                                option -> List.copyOf(submissionsByOptionId.getOrDefault(option.getId(), List.of())),
                                (existing, duplicate) -> existing,
                                LinkedHashMap::new
                        ))
        );
    }

    public List<QuizOption> getQuizOptions() {
        return List.copyOf(submissions.keySet());
    }

    public List<Long> getDistinctStudentIds() {
        return submissions.values().stream()
                .flatMap(List::stream)
                .map(QuizSubmission::getStudentId)
                .distinct()
                .toList();
    }

    public long countDistinctStudents() {
        return getDistinctStudentIds().size();
    }

    public List<QuizSubmission> getSubmissions(QuizOption option) {
        return submissions.getOrDefault(option, List.of());
    }

    public int calculateCorrectRate() {
        long totalSubmissions = submissions.values().stream()
                .mapToLong(List::size)
                .sum();

        if (totalSubmissions == 0) {
            return 0;
        }

        long correctSubmissions = submissions.entrySet().stream()
                .filter(entry -> entry.getKey().isCorrect())
                .mapToLong(entry -> entry.getValue().size())
                .sum();

        double rawRate = (double) correctSubmissions / totalSubmissions * 100;
        return (int) Math.round(rawRate);
    }

    public List<Long> getCorrectOptionIds() {
        return submissions.keySet().stream()
                .filter(QuizOption::isCorrect)
                .map(QuizOption::getId)
                .toList();
    }

    public List<Long> getSubmittedOptionIdsByStudentId(Long studentId) {
        return submissions.values().stream()
                .flatMap(List::stream)
                .filter(submission -> submission.getStudentId().equals(studentId))
                .map(QuizSubmission::getQuizOptionId)
                .toList();
    }
}
