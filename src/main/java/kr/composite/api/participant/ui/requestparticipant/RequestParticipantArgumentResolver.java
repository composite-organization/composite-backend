package kr.composite.api.participant.ui.requestparticipant;

import kr.composite.api.participant.application.ParticipantService;
import kr.composite.api.participant.domain.Participant;
import kr.composite.api.participant.domain.Student;
import kr.composite.api.participant.domain.Teacher;
import kr.composite.api.participant.ui.ParticipantUIException;
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
    private final ParticipantService participantService;

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
                .orElseThrow(ParticipantUIException::authRequired);
        Participant participant = participantService.getParticipant(userId);
        Class<?> parameterType = parameter.getParameterType();
        if (Student.class.equals(parameterType)) {
            return participantService.getStudent(participant);
        }
        if (Teacher.class.equals(parameterType)) {
            return participantService.getTeacher(participant);
        }
        if (Participant.class.isAssignableFrom(parameterType)) {
            return participant;
        }

        throw ParticipantUIException.unsupportedParticipantType();
    }
}
