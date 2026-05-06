package kr.composite.api.lesson.ui.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.Map;

@Schema(description = "수업 위젯 ID 목록 응답")
public record GetWidgetIdsResponse(
        @Schema(description = "타입별 위젯 고유 ID 목록 (key: 위젯 타입(memo, attachment, quiz, vote), value: 해당 타입의 고유 ID 목록)", 
                example = "{\"memo\": [1, 3], \"quiz\": [2], \"vote\": [4, 5], \"attachment\": []}")
        Map<String, List<Long>> widgets
) {

    public static GetWidgetIdsResponse from(Map<String, List<Long>> widgets) {
        return new GetWidgetIdsResponse(widgets);
    }
}
