package kr.composite.api.lesson.ui;

import jakarta.servlet.http.HttpServletResponse;
import kr.composite.api.lesson.application.LessonService;
import kr.composite.api.lesson.ui.apiSpec.LessonApiSpec;
import kr.composite.api.lesson.ui.dto.request.CreateLessonRequest;
import kr.composite.api.lesson.ui.dto.request.FindMyLessonRequest;
import kr.composite.api.lesson.ui.dto.request.JoinLessonRequest;
import kr.composite.api.lesson.ui.dto.response.CreateLessonResponse;
import kr.composite.api.lesson.ui.dto.response.GetLessonResponse;
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
    public ResponseEntity<Void> joinStudent(
            @RequestUser User user,
            @PathVariable("lessonCode") String lessonCode,
            @RequestBody JoinLessonRequest request
    ) {
        lessonService.joinStudent(lessonCode, user, request);

        return ResponseEntity.status(HttpStatus.CREATED).build();
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
    @PostMapping("/lessons/me")
    public ResponseEntity<Void> findMyLesson(
            HttpServletResponse response,
            @RequestBody FindMyLessonRequest request
    ) {
        String token = lessonService.findMyLesson(request);
        credentialTranslator.inject(response, token);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    @GetMapping("/lessons/{lessonCode}")
    public ResponseEntity<GetLessonResponse> getLesson(
            @PathVariable("lessonCode") String lessonCode,
            @RequestUser User user
    ) {
        GetLessonResponse getLessonResponse = lessonService.readLesson(lessonCode, user);

        return ResponseEntity.ok().body(getLessonResponse);
    }
}
