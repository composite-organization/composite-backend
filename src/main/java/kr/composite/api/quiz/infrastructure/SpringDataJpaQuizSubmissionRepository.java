package kr.composite.api.quiz.infrastructure;

import java.util.List;
import kr.composite.api.quiz.domain.QuizSubmission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataJpaQuizSubmissionRepository extends JpaRepository<QuizSubmission, Long> {

    Long countByQuizWidgetId(Long quizWidgetId);

    Long countByQuizWidgetIdAndQuizOptionIdIn(Long quizWidgetId, List<Long> quizOptionIds);

    Boolean existsByStudentIdAndQuizWidgetId(Long studentId, Long quizWidgetId);

    List<QuizSubmission> findAllByStudentIdAndQuizWidgetId(Long studentId, Long quizWidgetId);

    Boolean existsByQuizWidgetId(Long quizWidgetId);

    void deleteAllByQuizWidgetId(Long quizWidgetId);
}
