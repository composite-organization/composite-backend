package kr.composite.api.quiz.domain;

import java.util.Optional;

public interface QuizWidgetRepository {

    void save(QuizWidget quizWidget);

    Optional<QuizWidget> findById(Long quizWidgetId);

    Optional<QuizWidget> findByWidgetId(Long widgetId);

    void deleteById(Long quizWidgetId);
}
