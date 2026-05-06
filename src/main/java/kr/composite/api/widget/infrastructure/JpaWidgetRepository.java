package kr.composite.api.widget.infrastructure;

import java.util.List;
import java.util.Optional;
import kr.composite.api.widget.domain.Widget;
import kr.composite.api.widget.domain.WidgetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JpaWidgetRepository implements WidgetRepository {

    private final SpringDataJpaWidgetRepository springDataJpaWidgetRepository;

    @Override
    public void save(Widget widget) {
        springDataJpaWidgetRepository.save(widget);
    }

    @Override
    public void deleteById(Long id) {
        springDataJpaWidgetRepository.deleteById(id);
    }

    @Override
    public Optional<Widget> findById(Long id) {
        return springDataJpaWidgetRepository.findById(id);
    }

    @Override
    public List<Widget> findAllByLessonId(Long lessonId) {
        return springDataJpaWidgetRepository.findAllByLessonId(lessonId);
    }
}
