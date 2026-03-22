package kr.composite.api.widget.domain;

public interface WidgetRepository {

    void save(Widget widget);

    void deleteById(Long id);
}
