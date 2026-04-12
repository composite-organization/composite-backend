package kr.composite.api.quiz.ui;

import kr.composite.api.quiz.application.QuizWidgetService;
import kr.composite.api.quiz.ui.apiSpec.QuizWidgetApiSpec;
import kr.composite.api.quiz.ui.dto.request.CreateQuizWidgetRequest;
import kr.composite.api.quiz.ui.dto.request.SubmitQuizSubmissionRequest;
import kr.composite.api.quiz.ui.dto.request.UpdateQuizOptionRequest;
import kr.composite.api.quiz.ui.dto.request.UpdateQuizWidgetStatusRequest;
import kr.composite.api.quiz.ui.dto.response.CreateQuizWidgetResponse;
import kr.composite.api.quiz.ui.dto.response.GetQuizAnswerResponse;
import kr.composite.api.quiz.ui.dto.response.GetQuizResultResponse;
import kr.composite.api.quiz.ui.dto.response.GetQuizWidgetResponse;
import kr.composite.api.quiz.ui.dto.response.UpdateQuizOptionResponse;
import kr.composite.api.user.domain.User;
import kr.composite.api.user.ui.requestuser.RequestUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class QuizWidgetController implements QuizWidgetApiSpec {

    private final QuizWidgetService quizWidgetService;

    @Override
    @PostMapping("quizWidgets")
    public ResponseEntity<CreateQuizWidgetResponse> createQuizWidget(
            @RequestUser User user,
            @RequestBody CreateQuizWidgetRequest request
    ) {
        CreateQuizWidgetResponse createQuizWidgetResponse = quizWidgetService.addQuizWidget(user, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(createQuizWidgetResponse);
    }

    @Override
    @GetMapping("quizWidgets/{quizWidgetId}")
    public ResponseEntity<GetQuizWidgetResponse> getQuizWidget(
            @RequestUser User user,
            @PathVariable("quizWidgetId") Long quizWidgetId
    ) {
        GetQuizWidgetResponse getQuizWidgetResponse = quizWidgetService.readQuizWidget(user, quizWidgetId);

        return ResponseEntity.ok().body(getQuizWidgetResponse);
    }

    @Override
    @GetMapping("quizWidgets/{quizWidgetId}/answers")
    public ResponseEntity<GetQuizAnswerResponse> getQuizAnswer(
            @RequestUser User user,
            @PathVariable("quizWidgetId") Long quizWidgetId
    ) {
        GetQuizAnswerResponse getQuizAnswerResponse = quizWidgetService.readQuizAnswer(quizWidgetId);

        return ResponseEntity.ok().body(getQuizAnswerResponse);
    }

    @Override
    @PatchMapping("quizWidgets/{quizWidgetId}/status")
    public ResponseEntity<Void> updateQuizWidgetStatus(
            @RequestUser User user,
            @PathVariable("quizWidgetId") Long quizWidgetId,
            @RequestBody UpdateQuizWidgetStatusRequest request
    ) {
        quizWidgetService.updateQuizWidgetStatus(user, quizWidgetId, request);

        return ResponseEntity.noContent().build();
    }

    @Override
    @DeleteMapping("quizWidgets/{quizWidgetId}")
    public ResponseEntity<Void> deleteQuizWidget(
            @RequestUser User user,
            @PathVariable("quizWidgetId") Long quizWidgetId
    ) {
        quizWidgetService.deleteQuizWidget(user, quizWidgetId);

        return ResponseEntity.noContent().build();
    }

    @Override
    @PostMapping("quizWidgets/{quizWidgetId}/submissions")
    public ResponseEntity<Void> submitQuizSubmission(
            @RequestUser User user,
            @PathVariable("quizWidgetId") Long quizWidgetId,
            @RequestBody SubmitQuizSubmissionRequest request
    ) {
        quizWidgetService.submitQuizSubmission(user, quizWidgetId, request);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    @PatchMapping("quizWidgets/{quizWidgetId}/quizOptions")
    public ResponseEntity<UpdateQuizOptionResponse> updateQuizOption(
            @RequestUser User user,
            @RequestBody UpdateQuizOptionRequest request
    ) {
        UpdateQuizOptionResponse updateQuizOptionResponse = quizWidgetService.updateQuizOption(request);

        return ResponseEntity.ok().body(updateQuizOptionResponse);
    }
}
