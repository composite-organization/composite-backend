package kr.composite.api.widget.infrastructure;

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
}
