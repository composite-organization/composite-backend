package kr.composite.api.widget.infrastructure;

import java.util.List;
import kr.composite.api.widget.domain.Widget;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataJpaWidgetRepository extends JpaRepository<Widget, Long> {

    List<Widget> findAllByLessonId(Long lessonId);
}
