package kr.composite.api.memo.application;

import kr.composite.api.memo.application.dto.request.MemoWidgetAddRequest;
import kr.composite.api.memo.application.dto.request.MemoWidgetIdRequest;
import kr.composite.api.memo.application.dto.request.MemoWidgetUpdateRequest;
import kr.composite.api.memo.application.dto.response.MemoWidgetResponse;
import kr.composite.api.memo.domain.MemoContent;
import kr.composite.api.memo.domain.MemoTitle;
import kr.composite.api.memo.domain.MemoWidget;
import kr.composite.api.memo.domain.MemoWidgetRepository;
import kr.composite.api.widget.domain.Widget;
import kr.composite.api.widget.domain.WidgetRepository;
import kr.composite.api.widget.domain.WidgetType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemoWidgetService {

    private final MemoWidgetRepository memoWidgetRepository;
    private final WidgetRepository widgetRepository;

    @Transactional(readOnly = true)
    public MemoWidgetResponse getMemoWidget(MemoWidgetIdRequest request) {
        MemoWidget memoWidget = memoWidgetRepository.findById(request.id())
                .orElseThrow(() -> new IllegalArgumentException());

        MemoWidgetResponse memoWidgetResponse = MemoWidgetResponse.from(memoWidget);

        return memoWidgetResponse;
    }

    @Transactional
    public MemoWidgetResponse addMemoWidget(MemoWidgetAddRequest request) {
        Widget widget = new Widget(request.lessonId(), WidgetType.MEMO);
        widgetRepository.save(widget);

        MemoTitle memoTitle = new MemoTitle(request.title());
        MemoContent memoContent = new MemoContent(request.content());
        MemoWidget memoWidget = new MemoWidget(widget.getId(), memoTitle, memoContent);
        memoWidgetRepository.save(memoWidget);

        return MemoWidgetResponse.from(memoWidget);
    }

    @Transactional
    public MemoWidgetResponse updateMemoWidget(MemoWidgetUpdateRequest request) {
        MemoWidget memoWidget = memoWidgetRepository.findById(request.memoWidgetId())
                .orElseThrow(() -> new IllegalArgumentException());

        memoWidget.update(request.title(), request.content());

        MemoWidgetResponse memoWidgetResponse = MemoWidgetResponse.from(memoWidget);

        return memoWidgetResponse;
    }

    @Transactional
    public void deleteMemoWidget(MemoWidgetIdRequest request) {
        MemoWidget memoWidget = memoWidgetRepository.findById(request.id()).orElse(null);
        if (memoWidget == null) {
            return;
        }

        memoWidgetRepository.deleteById(request.id());
        widgetRepository.deleteById(memoWidget.getWidgetId());
    }
}
