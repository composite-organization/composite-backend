package kr.composite.api.quiz.infrastructure;

import kr.composite.api.quiz.domain.QuizWidget;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataJpaQuizWidgetRepository extends JpaRepository<QuizWidget, Long> {

}
