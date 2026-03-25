package kr.composite.api.user.ui.requestuser;

import java.util.Optional;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Component
@RequestScope
public class RequestUserIdContext {

    private Long value;

    public void set(Long value) {
        this.value = value;
    }

    public Optional<Long> get() {
        return Optional.ofNullable(value);
    }
}
