package kr.composite.api.vote.ui.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kr.composite.api.student.domain.ParticipatedStudent;
import kr.composite.api.vote.domain.VoteOption;
import kr.composite.api.vote.domain.VoteSubmission;
import kr.composite.api.vote.domain.VoteSubmissions;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "투표 현황 데이터 (IN_PROGRESS 상태)")
public record VoteInProgressData(

        @Schema(description = "총 참여 인원 수", example = "15")
        long totalParticipantCount,

        @Schema(description = "익명 투표 현황 (익명 투표인 경우)")
        List<AnonymousOptionStatus> anonymousOptionStatuses,

        @Schema(description = "실명 투표 현황 (실명 투표인 경우)")
        List<IdentifiedOptionStatus> identifiedOptionStatuses
) implements VoteStatusData {

    public static VoteInProgressData anonymous(
            List<VoteOption> voteOptions,
            VoteSubmissions voteSubmissions
    ) {
        Map<VoteOption, Long> submissionCountByOption = voteSubmissions.countByOptions(voteOptions);

        List<AnonymousOptionStatus> anonymousOptionStatuses = voteOptions.stream()
                .map(option -> AnonymousOptionStatus.of(option, submissionCountByOption))
                .toList();

        return new VoteInProgressData(voteSubmissions.countParticipants(), anonymousOptionStatuses, null);
    }

    public static VoteInProgressData identified(
            List<VoteOption> voteOptions,
            VoteSubmissions voteSubmissions,
            List<ParticipatedStudent> participatedStudents
    ) {
        Map<Long, String> studentNameMap = participatedStudents.stream()
                .collect(Collectors.toMap(
                        ParticipatedStudent::studentId,
                        ParticipatedStudent::studentName
                ));

        Map<VoteOption, List<VoteSubmission>> submissionsByOption = voteSubmissions.groupByOptions(voteOptions);

        List<IdentifiedOptionStatus> identifiedOptionStatuses = voteOptions.stream()
                .map(option -> IdentifiedOptionStatus.of(option, submissionsByOption, studentNameMap))
                .toList();

        return new VoteInProgressData(voteSubmissions.countParticipants(), null, identifiedOptionStatuses);
    }

    @Schema(description = "익명 투표 선택지별 현황")
    public record AnonymousOptionStatus(

            @Schema(description = "선택지 ID", example = "1")
            Long optionId,

            @Schema(description = "선택지 내용", example = "칼국수")
            String content,

            @Schema(description = "투표 인원 수", example = "5")
            long count
    ) {

        public static AnonymousOptionStatus of(VoteOption voteOption, Map<VoteOption, Long> submissionCountByOption) {
            return new AnonymousOptionStatus(
                    voteOption.getId(),
                    voteOption.getContent().getValue(),
                    submissionCountByOption.getOrDefault(voteOption, 0L)
            );
        }
    }

    @Schema(description = "실명 투표 선택지별 현황")
    public record IdentifiedOptionStatus(

            @Schema(description = "선택지 ID", example = "1")
            Long optionId,

            @Schema(description = "선택지 내용", example = "칼국수")
            String content,

            @Schema(description = "투표한 사용자 이름 목록")
            List<String> voterNames
    ) {

        public static IdentifiedOptionStatus of(VoteOption voteOption,
                                                Map<VoteOption, List<VoteSubmission>> submissionsByOption,
                                                Map<Long, String> studentNameMap) {
            List<String> voterNames = submissionsByOption.getOrDefault(voteOption, List.of()).stream()
                    .map(submission -> studentNameMap.getOrDefault(submission.getStudentId(), "알 수 없음"))
                    .toList();

            return new IdentifiedOptionStatus(
                    voteOption.getId(),
                    voteOption.getContent().getValue(),
                    voterNames
            );
        }
    }
}
