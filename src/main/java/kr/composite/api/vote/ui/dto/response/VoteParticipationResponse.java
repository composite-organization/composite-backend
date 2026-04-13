package kr.composite.api.vote.ui.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import kr.composite.api.student.domain.StudentName;
import kr.composite.api.student.domain.Students;
import kr.composite.api.vote.domain.VoteOption;
import kr.composite.api.vote.domain.VoteSubmissions;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "선택지별 투표 현황 (투표 진행·종료 상태와 무관하게 항상 포함됩니다)")
public record VoteParticipationResponse(

        @Schema(description = "총 투표 참여 인원 수", example = "15")
        long totalParticipantCount,

        @Schema(description = "선택지별 익명 현황 목록 (익명 투표인 경우에만 포함)")
        List<AnonymousOptionStatus> anonymousOptionStatuses,

        @Schema(description = "선택지별 실명 현황 목록 (실명 투표인 경우에만 포함)")
        List<IdentifiedOptionStatus> identifiedOptionStatuses
) {

    public static VoteParticipationResponse anonymous(VoteSubmissions voteSubmissions) {
        List<AnonymousOptionStatus> anonymousOptionStatuses = voteSubmissions.getVoteOptions().stream()
                .map(option -> AnonymousOptionStatus.of(option, voteSubmissions))
                .toList();

        return new VoteParticipationResponse(voteSubmissions.countDistinctStudents(), anonymousOptionStatuses, null);
    }

    public static VoteParticipationResponse identified(VoteSubmissions voteSubmissions, Students students) {
        List<IdentifiedOptionStatus> identifiedOptionStatuses = voteSubmissions.getVoteOptions().stream()
                .map(option -> IdentifiedOptionStatus.of(option, voteSubmissions, students))
                .toList();

        return new VoteParticipationResponse(voteSubmissions.countDistinctStudents(), null, identifiedOptionStatuses);
    }

    @Schema(description = "익명 투표 선택지별 현황")
    public record AnonymousOptionStatus(

            @Schema(description = "선택지 ID", example = "1")
            Long optionId,

            @Schema(description = "해당 선택지에 투표한 인원 수", example = "5")
            long count
    ) {

        public static AnonymousOptionStatus of(VoteOption voteOption, VoteSubmissions voteSubmissions) {
            return new AnonymousOptionStatus(voteOption.getId(), voteSubmissions.countSubmissions(voteOption));
        }
    }

    @Schema(description = "실명 투표 선택지별 현황")
    public record IdentifiedOptionStatus(

            @Schema(description = "선택지 ID", example = "1")
            Long optionId,

            @Schema(description = "해당 선택지에 투표한 학생 이름 목록")
            List<String> voterNames
    ) {

        private static final StudentName UNKNOWN_STUDENTNAME = new StudentName("알 수 없는 사용자");

        public static IdentifiedOptionStatus of(
                VoteOption voteOption,
                VoteSubmissions voteSubmissions,
                Students students
        ) {
            List<String> voterNames = voteSubmissions.getSubmissions(voteOption).stream()
                    .map(submission -> findStudentName(students, submission.getStudentId()))
                    .map(StudentName::getValue)
                    .toList();

            return new IdentifiedOptionStatus(voteOption.getId(), voterNames);
        }

        private static StudentName findStudentName(Students students, Long studentId) {
            return students.findName(studentId)
                    .orElse(UNKNOWN_STUDENTNAME);
        }
    }
}
