package kr.composite.api.user.ui.requestuser;

import kr.composite.api.user.domain.Guest;
import kr.composite.api.user.ui.UserUIException;
import kr.composite.api.user.domain.GuestRepository;
import kr.composite.api.user.domain.Member;
import kr.composite.api.user.domain.MemberRepository;
import kr.composite.api.user.domain.User;
import kr.composite.api.user.domain.UserRepository;
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
public class RequestUserArgumentResolver implements HandlerMethodArgumentResolver {

    private final RequestUserIdContext requestUserIdContext;
    private final UserRepository userRepository;
    private final MemberRepository memberRepository;
    private final GuestRepository guestRepository;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(RequestUser.class);
    }

    @Override
    public Object resolveArgument(
            @NonNull MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            @NonNull NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory
    ) {
        Long userId = requestUserIdContext.get()
                .orElseThrow(UserUIException::authRequired);
        Class<?> parameterType = parameter.getParameterType();
        if (User.class.equals(parameterType)) {
            return userRepository.findById(userId)
                    .orElseThrow(() -> UserUIException.userNotFound(userId));
        }
        if (Member.class.equals(parameterType)) {
            return memberRepository.findByUserId(userId)
                    .orElseThrow(() -> UserUIException.memberNotFound(userId));
        }
        if (Guest.class.equals(parameterType)) {
            return guestRepository.findByUserId(userId)
                    .orElseThrow(() -> UserUIException.guestNotFound(userId));
        }

        throw UserUIException.unsupportedUserType(parameterType.getSimpleName());
    }
}
