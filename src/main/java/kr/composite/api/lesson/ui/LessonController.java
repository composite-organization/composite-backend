package kr.composite.api.lesson.ui;

import jakarta.servlet.http.HttpServletResponse;
import kr.composite.api.lesson.application.AuthenticateLessonResult;
import kr.composite.api.lesson.application.LessonService;
import kr.composite.api.lesson.ui.apiSpec.LessonApiSpec;
import kr.composite.api.lesson.ui.dto.request.CreateLessonRequest;
import kr.composite.api.lesson.ui.dto.request.FindMyLessonRequest;
import kr.composite.api.lesson.ui.dto.request.JoinLessonRequest;
import kr.composite.api.lesson.ui.dto.response.AuthenticateLessonResponse;
import kr.composite.api.lesson.ui.dto.response.CreateLessonResponse;
import kr.composite.api.lesson.ui.dto.response.GetLessonResponse;
import kr.composite.api.lesson.ui.dto.response.GetWidgetIdsResponse;
import kr.composite.api.lesson.ui.dto.response.JoinStudentResponse;
import kr.composite.api.user.domain.User;
import kr.composite.api.user.ui.CredentialTranslator;
import kr.composite.api.user.ui.requestuser.RequestUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LessonController implements LessonApiSpec {

    private final LessonService lessonService;
    private final CredentialTranslator credentialTranslator;

    @Override
    @PostMapping("/lessons/{lessonCode}/students")
    public ResponseEntity<JoinStudentResponse> joinStudent(
            @RequestUser User user,
            @PathVariable("lessonCode") String lessonCode,
            @RequestBody JoinLessonRequest request
    ) {
        Long lessonId = lessonService.joinStudent(lessonCode, user, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(JoinStudentResponse.from(lessonId));
    }

    @Override
    @PostMapping("/lessons")
    public ResponseEntity<CreateLessonResponse> createLesson(
            @RequestUser User user,
            @RequestBody CreateLessonRequest request
    ) {
        CreateLessonResponse createLessonResponse = lessonService.createLesson(user, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(createLessonResponse);
    }

    @Override
    @PostMapping("/lessons/authentications")
    public ResponseEntity<AuthenticateLessonResponse> authenticateLesson(
            HttpServletResponse response,
            @RequestBody FindMyLessonRequest request
    ) {
        AuthenticateLessonResult result = lessonService.readMyLesson(request);
        credentialTranslator.inject(response, result.token());

        return ResponseEntity.status(HttpStatus.CREATED).body(AuthenticateLessonResponse.from(result.lessonId()));
    }

    @Override
    @GetMapping("/lessons/{lessonId}")
    public ResponseEntity<GetLessonResponse> getLesson(
            @PathVariable("lessonId") Long lessonId,
            @RequestUser User user
    ) {
        GetLessonResponse getLessonResponse = lessonService.readLesson(lessonId, user);

        return ResponseEntity.ok().body(getLessonResponse);
    }

    @Override
    @GetMapping("/lessons/{lessonId}/widgets")
    public ResponseEntity<GetWidgetIdsResponse> getWidgetIds(
            @PathVariable("lessonId") Long lessonId,
            @RequestUser User user
    ) {
        GetWidgetIdsResponse response = lessonService.readWidgetIds(lessonId, user);

        return ResponseEntity.ok().body(response);
    }
}
