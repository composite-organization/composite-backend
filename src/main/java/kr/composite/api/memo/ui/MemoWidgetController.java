package kr.composite.api.memo.ui;

import jakarta.validation.Valid;
import java.net.URI;
import kr.composite.api.memo.application.MemoWidgetService;
import kr.composite.api.memo.application.dto.request.MemoWidgetFindRequest;
import kr.composite.api.memo.ui.apiSpec.MemoWidgetApiSpec;
import kr.composite.api.memo.ui.dto.request.PostMemoWidgetRequest;
import kr.composite.api.memo.ui.dto.request.UpdateMemoWidgetRequest;
import kr.composite.api.memo.ui.dto.response.CreateMemoWidgetResponse;
import kr.composite.api.memo.ui.dto.response.GetMemoWidgetResponse;
import kr.composite.api.memo.ui.dto.response.UpdateMemoWidgetResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemoWidgetController implements MemoWidgetApiSpec {

    private final MemoWidgetService memoWidgetService;

    @GetMapping("memoWidgets/{memoWidgetId}")
    public ResponseEntity<GetMemoWidgetResponse> readMemoWidget(@PathVariable("memoWidgetId") Long memoWidgetId) {
        MemoWidgetFindRequest memoWidgetFindRequest = MemoWidgetFindRequest.from(memoWidgetId);
        GetMemoWidgetResponse getMemoWidgetResponse = GetMemoWidgetResponse.from(
                memoWidgetService.getMemoWidget(memoWidgetFindRequest));

        return ResponseEntity.ok(getMemoWidgetResponse);
    }

    @PostMapping("memoWidgets")
    public ResponseEntity<CreateMemoWidgetResponse> createMemoWidget(
            @Valid @RequestBody PostMemoWidgetRequest request) {
        CreateMemoWidgetResponse createMemoWidgetResponse = CreateMemoWidgetResponse.from(
                memoWidgetService.addMemoWidget(request.toMemoWidgetAddRequest()));

        return ResponseEntity.created(URI.create("memoWidgets/" + createMemoWidgetResponse.id()))
                .body(createMemoWidgetResponse);
    }

    @PutMapping("memoWidgets/{memoWidgetId}")
    public ResponseEntity<UpdateMemoWidgetResponse> updateMemoWidget(
            @PathVariable("memoWidgetId") Long memoWidgetId,
            @Valid @RequestBody UpdateMemoWidgetRequest request
    ) {
        UpdateMemoWidgetResponse updateMemoWidgetResponse = UpdateMemoWidgetResponse.from(
                memoWidgetService.updateMemoWidget(request.toMemoWidgetUpdateRequest(memoWidgetId))
        );

        return ResponseEntity.ok().body(updateMemoWidgetResponse);
    }

    @DeleteMapping("memoWidgets/{memoWidgetId}")
    public ResponseEntity<Void> deleteMemoWidget(@PathVariable("memoWidgetId") Long memoWidgetId) {
        MemoWidgetFindRequest memoWidgetFindRequest = MemoWidgetFindRequest.from(memoWidgetId);
        memoWidgetService.deleteMemoWidget(memoWidgetFindRequest);

        return ResponseEntity.noContent().build();
    }
}
