package kr.composite.api.participant.ui.requestparticipant;

import kr.composite.api.participant.domain.Participant;
import kr.composite.api.participant.domain.ParticipantRepository;
import kr.composite.api.participant.domain.Student;
import kr.composite.api.participant.domain.StudentRepository;
import kr.composite.api.participant.domain.Teacher;
import kr.composite.api.participant.domain.TeacherRepository;
import kr.composite.api.user.ui.requestuser.RequestUserIdContext;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@RequiredArgsConstructor
public class RequestParticipantArgumentResolver implements HandlerMethodArgumentResolver {

    private final RequestUserIdContext requestUserIdContext;
    private final ParticipantRepository participantRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(RequestParticipant.class);
    }

    @Override
    public Object resolveArgument(
            @NonNull MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            @NonNull NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory
    ) {
        Long userId = requestUserIdContext.get()
                .orElseThrow(() -> new IllegalArgumentException("인증 정보가 필요한 요청입니다."));
        Participant participant = participantRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("참여자 정보가 존재하지 않습니다."));
        Class<?> parameterType = parameter.getParameterType();
        if (Student.class.equals(parameterType)) {
            return studentRepository.findByParticipantId(participant.getId())
                    .orElseThrow(() -> new IllegalArgumentException("해당 참여자는 학생이 아닙니다."));
        }
        if (Teacher.class.equals(parameterType)) {
            return teacherRepository.findByParticipantId(participant.getId())
                    .orElseThrow(() -> new IllegalArgumentException("해당 참여자는 수업자가 아닙니다."));
        }
        if (Participant.class.isAssignableFrom(parameterType)) {
            return participant;
        }

        throw new IllegalArgumentException("지원하지 않는 @RequestParticipant 타입입니다: " + parameterType.getSimpleName());
    }
}
