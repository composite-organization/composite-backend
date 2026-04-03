package kr.composite.api.quiz.infrastructure;

import java.util.Optional;
import kr.composite.api.quiz.domain.QuizWidget;
import kr.composite.api.quiz.domain.QuizWidgetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JpaQuizWidgetRepository implements QuizWidgetRepository {

    private final SpringDataJpaQuizWidgetRepository springDataJpaQuizWidgetRepository;

    @Override
    public void save(QuizWidget quizWidget) {
        springDataJpaQuizWidgetRepository.save(quizWidget);
    }

    @Override
    public Optional<QuizWidget> findById(Long quizWidgetId) {
        return springDataJpaQuizWidgetRepository.findById(quizWidgetId);
    }

    @Override
    public void deleteById(Long quizWidgetId) {
        springDataJpaQuizWidgetRepository.deleteById(quizWidgetId);
    }
}
