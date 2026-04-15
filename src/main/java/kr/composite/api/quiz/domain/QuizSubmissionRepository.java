package kr.composite.api.quiz.domain;

import java.util.List;

public interface QuizSubmissionRepository {

    void save(QuizSubmission quizSubmission);

    Long countByQuizWidgetId(Long quizWidgetId);

    Long countByQuizWidgetIdAndQuizOptionIdIn(Long quizWidgetId, List<Long> quizOptionIds);

    Boolean existsByStudentIdAndQuizWidgetId(Long studentId, Long quizWidgetId);

    void saveAll(List<QuizSubmission> submissions);

    Boolean existsByQuizWidgetId(Long quizWidgetId);

    void deleteAllByQuizWidgetId(Long quizWidgetId);
}
