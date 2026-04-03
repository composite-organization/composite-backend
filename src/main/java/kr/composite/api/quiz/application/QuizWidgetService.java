package kr.composite.api.quiz.application;

import java.util.List;
import java.util.Objects;
import kr.composite.api.quiz.domain.QuizOption;
import kr.composite.api.quiz.domain.QuizOptionRepository;
import kr.composite.api.quiz.domain.QuizStatus;
import kr.composite.api.quiz.domain.QuizSubmission;
import kr.composite.api.quiz.domain.QuizSubmissionRepository;
import kr.composite.api.quiz.domain.QuizTitle;
import kr.composite.api.quiz.domain.QuizWidget;
import kr.composite.api.quiz.domain.QuizWidgetRepository;
import kr.composite.api.quiz.ui.dto.request.CreateQuizWidgetRequest;
import kr.composite.api.quiz.ui.dto.request.SubmitQuizSubmissionRequest;
import kr.composite.api.quiz.ui.dto.request.UpdateQuizOptionRequest;
import kr.composite.api.quiz.ui.dto.request.UpdateQuizOptionRequest.QuizOptionRequest;
import kr.composite.api.quiz.ui.dto.request.UpdateQuizWidgetStatusRequest;
import kr.composite.api.quiz.ui.dto.response.CreateQuizWidgetResponse;
import kr.composite.api.quiz.ui.dto.response.GetQuizAnswerResponse;
import kr.composite.api.quiz.ui.dto.response.GetQuizResultResponse;
import kr.composite.api.quiz.ui.dto.response.GetQuizWidgetResponse;
import kr.composite.api.quiz.ui.dto.response.UpdateQuizOptionResponse;
import kr.composite.api.user.domain.User;
import kr.composite.api.widget.domain.Widget;
import kr.composite.api.widget.domain.WidgetRepository;
import kr.composite.api.widget.domain.WidgetType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class QuizWidgetService {

    private final QuizWidgetRepository quizWidgetRepository;
    private final WidgetRepository widgetRepository;
    private final QuizOptionRepository quizOptionRepository;
    private final QuizSubmissionRepository quizSubmissionRepository;

    @Transactional
    public CreateQuizWidgetResponse addQuizWidget(User user, CreateQuizWidgetRequest request) {
        Widget widget = new Widget(request.lessonId(), WidgetType.QUIZ);
        widgetRepository.save(widget);

        QuizTitle quizTitle = new QuizTitle(request.title());
        QuizWidget quizWidget = new QuizWidget(widget.getId(), quizTitle, QuizStatus.NOT_STARTED);
        quizWidgetRepository.save(quizWidget);

        List<QuizOption> quizOptions = request.options().stream()
                .map((option) -> new QuizOption(quizWidget.getId(), option.content(), option.isCorrect()))
                .toList();

        quizOptionRepository.saveAll(quizOptions);

        CreateQuizWidgetResponse createQuizWidgetResponse = CreateQuizWidgetResponse.from(quizWidget.getId());

        return createQuizWidgetResponse;
    }

    @Transactional(readOnly = true)
    public GetQuizWidgetResponse readQuizWidget(User user, Long quizWidgetId) {

        QuizWidget quizWidget = quizWidgetRepository.findById(quizWidgetId)
                .orElseThrow(() -> QuizWidgetApplicationException.cannotFindQuizWidget());

        List<QuizOption> quizOptions = quizOptionRepository.findAllByQuizWidgetId(quizWidgetId);

        return GetQuizWidgetResponse.of(quizWidget, quizOptions);
    }

    @Transactional
    public void updateQuizWidgetStatus(User user, Long quizWidgetId, UpdateQuizWidgetStatusRequest request) {
        QuizWidget quizWidget = quizWidgetRepository.findById(quizWidgetId)
                .orElseThrow(() -> QuizWidgetApplicationException.cannotFindQuizWidget());

        QuizStatus newStatus = QuizStatus.fromDescription(request.status());
        quizWidget.updateStatus(newStatus);

        quizWidgetRepository.save(quizWidget);
    }

    @Transactional
    public void deleteQuizWidget(Long quizWidgetId) {
        quizOptionRepository.deleteAllByQuizWidgetId(quizWidgetId);
        quizWidgetRepository.deleteById(quizWidgetId);
    }

    @Transactional
    public void submitQuizSubmission(User user, Long quizWidgetId, SubmitQuizSubmissionRequest request) {
        QuizWidget quizWidget = quizWidgetRepository.findById(quizWidgetId)
                .orElseThrow(() -> QuizWidgetApplicationException.cannotFindQuizWidget());

        if (!quizWidget.getQuizStatus().equals(QuizStatus.IN_PROGRESS)) {
            throw QuizWidgetApplicationException.invalidQuizStatus();
        }

        //TODO: user.getId -> student.getId
        if (quizSubmissionRepository.existsByStudentIdAndQuizWidgetId(user.getId(), quizWidgetId)) {
            throw QuizWidgetApplicationException.alreadySubmitted();
        }

        List<QuizOption> quizOptions = quizOptionRepository.findAllById(request.quizOptionIds());
        if (quizOptions.size() != request.quizOptionIds().size()) {
            throw QuizWidgetApplicationException.cannotFindQuizOption();
        }

        // TODO: studentId 관련 추가 작업 필요 현재 user.id 를 주입 중
        List<QuizSubmission> submissions = request.quizOptionIds().stream()
                .map(optionId -> new QuizSubmission(user.getId(), quizWidgetId, optionId))
                .toList();

        quizSubmissionRepository.saveAll(submissions);
    }

    @Transactional(readOnly = true)
    public GetQuizResultResponse getQuizResult(User user, Long quizWidgetId) {
        quizWidgetRepository.findById(quizWidgetId)
                .orElseThrow(() -> QuizWidgetApplicationException.cannotFindQuizWidget());

        Long totalSubmissions = quizSubmissionRepository.countByQuizWidgetId(quizWidgetId);
        if (totalSubmissions == 0) {
            return GetQuizResultResponse.from(0);
        }

        List<QuizOption> correctOptions = quizOptionRepository.findAllByQuizWidgetIdAndIsCorrectTrue(quizWidgetId);
        List<Long> correctOptionIds = correctOptions.stream()
                .map(QuizOption::getId)
                .toList();

        if (correctOptionIds.isEmpty()) {
            return GetQuizResultResponse.from(0);
        }

        Long correctSubmissions = quizSubmissionRepository.countByQuizWidgetIdAndQuizOptionIdIn(quizWidgetId,
                correctOptionIds);

        double rawRate = (double) correctSubmissions / totalSubmissions * 100;
        int correctRate = (int) Math.round(rawRate);

        return GetQuizResultResponse.from(correctRate);
    }

    @Transactional(readOnly = true)
    public GetQuizAnswerResponse readQuizAnswer(Long quizWidgetId) {
        List<QuizOption> quizOptions = quizOptionRepository.findAllByQuizWidgetIdAndIsCorrectTrue(quizWidgetId);

        List<Long> correctOptionIds = quizOptions.stream()
                .map(QuizOption::getId)
                .toList();

        return GetQuizAnswerResponse.from(correctOptionIds);
    }

    @Transactional
    public UpdateQuizOptionResponse updateQuizOption(UpdateQuizOptionRequest request) {
        Long quizWidgetId = request.quizWidgetId();

        if (quizSubmissionRepository.countByQuizWidgetId(quizWidgetId) > 0) {
            throw QuizWidgetApplicationException.cannotUpdateQuizOption();
        }

        List<QuizOption> existingOptions = quizOptionRepository.findAllByQuizWidgetId(quizWidgetId);

        // 삭제 처리
        List<Long> requestOptionIds = request.options().stream()
                .map(QuizOptionRequest::quizOptionId)
                .filter(Objects::nonNull)
                .toList();

        List<QuizOption> toDelete = existingOptions.stream()
                .filter(option -> !requestOptionIds.contains(option.getId()))
                .toList();
        quizOptionRepository.deleteAllInBatch(toDelete);

        // 업데이트 또는 생성 처리
        for (QuizOptionRequest optionRequest : request.options()) {
            if (optionRequest.quizOptionId() != null) {
                QuizOption existingOption = existingOptions.stream()
                        .filter(option -> option.getId().equals(optionRequest.quizOptionId()))
                        .findFirst()
                        .orElseThrow(QuizWidgetApplicationException::cannotFindQuizOption);

                existingOption.update(optionRequest.content(), optionRequest.isCorrect());
                quizOptionRepository.save(existingOption);
            } else {
                QuizOption newOption = new QuizOption(quizWidgetId, optionRequest.content(), optionRequest.isCorrect());
                quizOptionRepository.save(newOption);
            }
        }

        UpdateQuizOptionResponse updateQuizOptionResponse = UpdateQuizOptionResponse.from(quizWidgetId);

        return updateQuizOptionResponse;
    }
}
