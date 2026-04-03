package kr.composite.api.quiz.infrastructure;

import java.util.List;
import kr.composite.api.quiz.domain.QuizOption;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataJpaQuizOptionRepository extends JpaRepository<QuizOption, Long> {

    List<QuizOption> findAllByQuizWidgetId(Long quizWidgetId);

    void deleteAllByQuizWidgetId(Long quizWidgetId);

    List<QuizOption> findAllByQuizWidgetIdAndIsCorrectTrue(Long quizWidgetId);
}
