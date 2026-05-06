package kr.composite.api.quiz.domain;

import java.util.List;
import java.util.Optional;

public interface QuizWidgetRepository {

    void save(QuizWidget quizWidget);

    Optional<QuizWidget> findById(Long quizWidgetId);

    List<QuizWidget> findAllByWidgetIdIn(List<Long> widgetIds);

    void deleteById(Long quizWidgetId);
}
