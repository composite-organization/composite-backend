package kr.composite.api.memo.infrastructure;

import java.util.Optional;
import kr.composite.api.memo.domain.MemoWidget;
import kr.composite.api.memo.domain.MemoWidgetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JpaMemoWidgetRepository implements MemoWidgetRepository {

    private final SpringDataJpaMemoWidgetRepository springDataJpaMemoWidgetRepository;

    @Override
    public Optional<MemoWidget> findById(Long memoWidgetId) {
        return springDataJpaMemoWidgetRepository.findById(memoWidgetId);
    }

    @Override
    public void save(MemoWidget memoWidget) {
        springDataJpaMemoWidgetRepository.save(memoWidget);
    }

    @Override
    public void deleteById(Long memoWidgetId) {
        springDataJpaMemoWidgetRepository.deleteById(memoWidgetId);
    }
}
