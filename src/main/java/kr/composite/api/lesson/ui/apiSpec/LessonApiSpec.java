package kr.composite.api.lesson.ui.apiSpec;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import kr.composite.api.lesson.ui.dto.request.CreateLessonRequest;
import kr.composite.api.lesson.ui.dto.request.FindMyLessonRequest;
import kr.composite.api.lesson.ui.dto.request.JoinLessonRequest;
import kr.composite.api.lesson.ui.dto.response.CreateLessonResponse;
import kr.composite.api.lesson.ui.dto.response.GetLessonResponse;
import kr.composite.api.user.domain.User;
import org.springframework.http.ResponseEntity;

@Tag(name = "수업 API", description = "수업 및 참여 관련 API 명세입니다.")
public interface LessonApiSpec {

    @Operation(summary = "학생 참여", description = "수업에 학생으로 참여합니다.")
    ResponseEntity<Void> joinStudent(
            User user,
            @Parameter(description = "수업 코드", example = "ABCD12") String lessonCode,
            JoinLessonRequest request
    );

    @Operation(summary = "학생 참여", description = "수업에 학생으로 참여합니다.")
    ResponseEntity<CreateLessonResponse> createLesson(
            User user,
            CreateLessonRequest request
    );

    @Operation(summary = "내 수업 찾기", description = "수업자 자신이 만든 수업을 찾습니다.")
    ResponseEntity<Void> getMyLesson(
            HttpServletResponse response,
            FindMyLessonRequest request
    );

    @Operation(summary = "수업 조회", description = "수업 정보를 조회합니다.")
    ResponseEntity<GetLessonResponse> getLesson(
            @Parameter(description = "수업 코드", example = "ABCD12") String lessonCode,
            User user
    );
}
