package kr.composite.api.widget.domain;

import java.util.Optional;

public interface WidgetRepository {

    void save(Widget widget);

    void deleteById(Long id);

    Optional<Widget> findById(Long id);
}
