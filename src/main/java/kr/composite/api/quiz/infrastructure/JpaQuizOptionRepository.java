package kr.composite.api.quiz.infrastructure;

import java.util.List;
import java.util.Optional;
import kr.composite.api.quiz.domain.QuizOption;
import kr.composite.api.quiz.domain.QuizOptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JpaQuizOptionRepository implements QuizOptionRepository {

    private final SpringDataJpaQuizOptionRepository springDataJpaQuizOptionRepository;

    @Override
    public void saveAll(List<QuizOption> quizOptions) {
        springDataJpaQuizOptionRepository.saveAll(quizOptions);
    }

    @Override
    public void save(QuizOption quizOption) {
        springDataJpaQuizOptionRepository.save(quizOption);
    }

    @Override
    public List<QuizOption> findAllByQuizWidgetId(Long quizWidgetId) {
        return springDataJpaQuizOptionRepository.findAllByQuizWidgetId(quizWidgetId);
    }

    @Override
    public void deleteAllByQuizWidgetId(Long quizWidgetId) {
        springDataJpaQuizOptionRepository.deleteAllByQuizWidgetId(quizWidgetId);
    }

    @Override
    public Optional<QuizOption> findById(Long id) {
        return springDataJpaQuizOptionRepository.findById(id);
    }

    @Override
    public List<QuizOption> findAllByQuizWidgetIdAndIsCorrectTrue(Long quizWidgetId) {
        return springDataJpaQuizOptionRepository.findAllByQuizWidgetIdAndIsCorrectTrue(quizWidgetId);
    }

    @Override
    public void deleteAllInBatch(List<QuizOption> toDelete) {
        springDataJpaQuizOptionRepository.deleteAllInBatch(toDelete);
    }
}
