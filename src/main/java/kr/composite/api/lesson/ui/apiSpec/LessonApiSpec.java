package kr.composite.api.lesson.ui.apiSpec;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.composite.api.lesson.ui.dto.request.JoinLessonRequest;
import kr.composite.api.user.domain.User;
import org.springframework.http.ResponseEntity;

@Tag(name = "수업 API", description = "수업 및 참여 관련 API 명세입니다.")
public interface LessonApiSpec {

    @Operation(summary = "학생 참여", description = "수업에 학생으로 참여합니다.")
    ResponseEntity<Void> joinStudent(
            User user,
            @Parameter(description = "수업 ID", example = "1") Long lessonId,
            JoinLessonRequest request
    );
}
