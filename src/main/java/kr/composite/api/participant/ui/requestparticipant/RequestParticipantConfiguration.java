package kr.composite.api.participant.ui.requestparticipant;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class RequestParticipantConfiguration implements WebMvcConfigurer {

    private final RequestParticipantArgumentResolver requestParticipantArgumentResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(requestParticipantArgumentResolver);
    }
}
