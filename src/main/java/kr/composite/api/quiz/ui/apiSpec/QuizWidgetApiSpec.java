package kr.composite.api.quiz.ui.apiSpec;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import org.springframework.http.ResponseEntity;

@Tag(name = "퀴즈 위젯 API", description = "퀴즈 생성, 조회, 제출 및 결과 확인을 위한 API 명세입니다.")
public interface QuizWidgetApiSpec {

    @Operation(summary = "퀴즈 위젯 생성", description = "새로운 퀴즈 위젯을 생성합니다.")
    ResponseEntity<CreateQuizWidgetResponse> createQuizWidget(
            User user,
            CreateQuizWidgetRequest request
    );

    @Operation(summary = "퀴즈 위젯 조회", description = "퀴즈 위젯 ID로 퀴즈 정보와 옵션 목록을 조회합니다.")
    ResponseEntity<GetQuizWidgetResponse> getQuizWidget(
            User user,
            @Parameter(description = "퀴즈 위젯 ID", example = "1") Long quizWidgetId
    );

    @Operation(summary = "퀴즈 정답 조회", description = "퀴즈의 정답 옵션 ID 목록을 조회합니다.")
    ResponseEntity<GetQuizAnswerResponse> getQuizAnswer(
            User user,
            @Parameter(description = "퀴즈 위젯 ID", example = "1") Long quizWidgetId
    );

    @Operation(summary = "퀴즈 상태 수정", description = "퀴즈의 상태(시작 전, 진행 중, 종료)를 변경합니다.")
    ResponseEntity<Void> updateQuizWidgetStatus(
            User user,
            @Parameter(description = "퀴즈 위젯 ID", example = "1") Long quizWidgetId,
            UpdateQuizWidgetStatusRequest request
    );

    @Operation(summary = "퀴즈 위젯 삭제", description = "퀴즈 위젯과 관련된 모든 옵션을 삭제합니다.")
    ResponseEntity<Void> deleteQuizWidget(
            User user,
            @Parameter(description = "퀴즈 위젯 ID", example = "1") Long quizWidgetId
    );

    @Operation(summary = "퀴즈 답안 제출", description = "학생이 선택한 퀴즈 답안을 제출합니다. (이미 제출한 학생은 불가)")
    ResponseEntity<Void> submitQuizSubmission(
            User user,
            @Parameter(description = "퀴즈 위젯 ID", example = "1") Long quizWidgetId,
            SubmitQuizSubmissionRequest request
    );

    @Operation(summary = "퀴즈 결과 조회", description = "해당 퀴즈의 전체 정답률을 조회합니다.")
    ResponseEntity<GetQuizResultResponse> getQuizResult(
            User user,
            @Parameter(description = "퀴즈 위젯 ID", example = "1") Long quizWidgetId
    );

    @Operation(summary = "퀴즈 옵션 수정", description = "퀴즈의 선택지 문항을 수정, 추가 또는 삭제합니다. (제출자가 있을 경우 수정 불가)")
    ResponseEntity<UpdateQuizOptionResponse> updateQuizOption(
            User user,
            UpdateQuizOptionRequest request
    );
}
