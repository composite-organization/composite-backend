package kr.composite.api.quiz.infrastructure;

import java.util.List;
import kr.composite.api.quiz.domain.QuizSubmission;
import kr.composite.api.quiz.domain.QuizSubmissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JpaQuizSubmissionRepository implements QuizSubmissionRepository {

    private final SpringDataJpaQuizSubmissionRepository springDataJpaQuizSubmissionRepository;

    @Override
    public void save(QuizSubmission quizSubmission) {
        springDataJpaQuizSubmissionRepository.save(quizSubmission);
    }

    @Override
    public Long countByQuizWidgetId(Long quizWidgetId) {
        return springDataJpaQuizSubmissionRepository.countByQuizWidgetId(quizWidgetId);
    }

    @Override
    public Long countByQuizWidgetIdAndQuizOptionIdIn(Long quizWidgetId, List<Long> quizOptionIds) {
        return springDataJpaQuizSubmissionRepository.countByQuizWidgetIdAndQuizOptionIdIn(quizWidgetId, quizOptionIds);
    }

    @Override
    public Boolean existsByStudentIdAndQuizWidgetId(Long studentId, Long quizWidgetId) {
        return springDataJpaQuizSubmissionRepository.existsByStudentIdAndQuizWidgetId(studentId, quizWidgetId);
    }
}
