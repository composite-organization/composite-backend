package kr.composite.api.user.ui.requestuser;

import kr.composite.api.user.application.UserService;
import kr.composite.api.user.domain.Guest;
import kr.composite.api.user.domain.Member;
import kr.composite.api.user.domain.User;
import kr.composite.api.user.ui.UserUIException;
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
    private final UserService userService;

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
        User user = userService.getUser(userId);
        Class<?> parameterType = parameter.getParameterType();
        if (User.class.equals(parameterType)) {
            return user;
        }
        if (Member.class.equals(parameterType)) {
            return userService.getMember(user);
        }
        if (Guest.class.equals(parameterType)) {
            return userService.getGuest(user);
        }

        throw UserUIException.unsupportedUserType();
    }
}
