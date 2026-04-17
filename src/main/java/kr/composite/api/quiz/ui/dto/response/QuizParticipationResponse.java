package kr.composite.api.quiz.ui.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import kr.composite.api.quiz.domain.QuizOption;
import kr.composite.api.quiz.domain.QuizSubmission;
import kr.composite.api.student.domain.StudentName;
import kr.composite.api.student.domain.Students;

@Schema(description = "퀴즈 참여 현황")
public record QuizParticipationResponse(
        @Schema(description = "총 참여 인원 수", example = "10")
        long totalParticipantCount,

        @Schema(description = "선택지별 참여 현황")
        List<QuizOptionStatus> optionStatuses
) {

    public static QuizParticipationResponse of(
            List<QuizOption> quizOptions,
            List<QuizSubmission> quizSubmissions,
            Students students
    ) {
        long totalParticipantCount = quizSubmissions.stream()
                .map(QuizSubmission::getStudentId)
                .distinct()
                .count();

        List<QuizOptionStatus> optionStatuses = quizOptions.stream()
                .map(option -> QuizOptionStatus.of(option, quizSubmissions, students))
                .toList();

        return new QuizParticipationResponse(totalParticipantCount, optionStatuses);
    }

    @Schema(description = "퀴즈 선택지별 참여 현황")
    public record QuizOptionStatus(
            @Schema(description = "선택지 ID", example = "1")
            Long optionId,

            @Schema(description = "해당 선택지를 선택한 학생 이름 목록")
            List<String> participantNames
    ) {
        private static final StudentName UNKNOWN_STUDENTNAME = new StudentName("알 수 없는 사용자");

        public static QuizOptionStatus of(
                QuizOption quizOption,
                List<QuizSubmission> quizSubmissions,
                Students students
        ) {
            List<String> participantNames = quizSubmissions.stream()
                    .filter(submission -> submission.getQuizOptionId().equals(quizOption.getId()))
                    .map(submission -> students.findName(submission.getStudentId())
                            .orElse(UNKNOWN_STUDENTNAME)
                            .getValue())
                    .toList();

            return new QuizOptionStatus(quizOption.getId(), participantNames);
        }
    }
}
