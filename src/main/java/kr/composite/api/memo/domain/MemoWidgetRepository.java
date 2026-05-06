package kr.composite.api.memo.domain;

import java.util.Optional;

public interface MemoWidgetRepository {

    Optional<MemoWidget> findById(Long memoWidgetId);

    Optional<MemoWidget> findByWidgetId(Long widgetId);

    void save(MemoWidget memoWidget);

    void deleteById(Long id);
}
