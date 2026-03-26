package kr.composite.api.lesson.ui;

import kr.composite.api.lesson.application.LessonService;
import kr.composite.api.lesson.ui.apiSpec.LessonApiSpec;
import kr.composite.api.lesson.ui.dto.request.JoinLessonRequest;
import kr.composite.api.user.domain.User;
import kr.composite.api.user.ui.requestuser.RequestUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/lessons/{lessonId}")
public class LessonController implements LessonApiSpec {

    private final LessonService lessonService;

    @Override
    @PostMapping("/students")
    public ResponseEntity<Void> joinStudent(
            @RequestUser User user,
            @PathVariable("lessonId") Long lessonId,
            @RequestBody JoinLessonRequest request
    ) {
        lessonService.joinStudent(lessonId, user, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
