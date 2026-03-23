package kr.composite.api.memo.application.dto.response;

import java.time.LocalDateTime;
import kr.composite.api.memo.domain.MemoWidget;

public record MemoWidgetResponse(
        Long id,
        Long widgetId,
        String title,
        String content,
        LocalDateTime updatedTime
) {

    public static MemoWidgetResponse from(MemoWidget memo) {
        return new MemoWidgetResponse(
                memo.getId(),
                memo.getWidgetId(),
                memo.getTitle().getValue(),
                memo.getContent().getValue(),
                memo.getUpdatedAt()
        );
    }
}
