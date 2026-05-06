package kr.composite.api.memo.infrastructure;

import java.util.List;
import java.util.Optional;
import kr.composite.api.memo.domain.MemoWidget;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataJpaMemoWidgetRepository extends JpaRepository<MemoWidget, Long> {

    List<MemoWidget> findAllByWidgetIdIn(List<Long> widgetIds);
}
