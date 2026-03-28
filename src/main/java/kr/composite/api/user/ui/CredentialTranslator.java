package kr.composite.api.user.ui;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component
public class CredentialTranslator {

    private static final String BEARER_PREFIX = "Bearer ";

    public Optional<String> extract(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(HttpHeaders.AUTHORIZATION))
                .filter(header -> header.startsWith(BEARER_PREFIX))
                .map(header -> header.substring(BEARER_PREFIX.length()));
    }

    public void inject(HttpServletResponse response, String credential) {
        response.setHeader(HttpHeaders.AUTHORIZATION, credential);
    }
}
