package kr.composite.api.memo.infrastructure;

import kr.composite.api.memo.domain.MemoWidget;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataJpaMemoWidgetRepository extends JpaRepository<MemoWidget, Long> {

}
