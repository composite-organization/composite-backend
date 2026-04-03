package kr.composite.api.quiz.domain;

import java.util.Optional;

public interface QuizWidgetRepository {

    void save(QuizWidget quizWidget);

    Optional<QuizWidget> findById(Long quizWidgetId);

    void deleteById(Long quizWidgetId);
}
