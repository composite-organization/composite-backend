package kr.composite.api.quiz.domain;

import java.util.List;
import java.util.Optional;

public interface QuizOptionRepository {

    void saveAll(List<QuizOption> quizOptions);

    void save(QuizOption quizOption);

    List<QuizOption> findAllByQuizWidgetId(Long quizWidgetId);

    void deleteAllByQuizWidgetId(Long quizWidgetId);

    Optional<QuizOption> findById(Long id);

    List<QuizOption> findAllByQuizWidgetIdAndIsCorrectTrue(Long quizWidgetId);

    void deleteAllInBatch(List<QuizOption> toDelete);

    List<QuizOption> findAllById(List<Long> ids);
}
