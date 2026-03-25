package kr.composite.api.user.ui.requestuser;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.composite.api.authentication.domain.CredentialCodec;
import kr.composite.api.user.ui.CredentialTranslator;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class RequestUserInterceptor implements HandlerInterceptor {

    private final CredentialCodec credentialCodec;
    private final RequestUserIdContext requestUserIdContext;
    private final CredentialTranslator credentialTranslator;

    @Override
    public boolean preHandle(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull Object handler
    ) {
        credentialTranslator.extract(request)
                .map(credentialCodec::decode)
                .ifPresent(credentialPayload -> requestUserIdContext.set(credentialPayload.identifier()));

        return true;
    }
}
