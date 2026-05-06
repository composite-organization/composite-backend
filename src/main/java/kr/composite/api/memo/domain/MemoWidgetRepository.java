package kr.composite.api.memo.domain;

import java.util.List;
import java.util.Optional;

public interface MemoWidgetRepository {

    Optional<MemoWidget> findById(Long memoWidgetId);

    List<MemoWidget> findAllByWidgetIdIn(List<Long> widgetIds);

    void save(MemoWidget memoWidget);

    void deleteById(Long id);
}
