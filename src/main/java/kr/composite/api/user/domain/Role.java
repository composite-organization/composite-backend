package kr.composite.api.user.domain;

import java.util.Arrays;
import lombok.Getter;

@Getter
public enum Role {

    GUEST("GUEST"),
    MEMBER("MEMBER"),
    USER("USER"); // User도 추가

    private final String description;

    Role(String description) {
        this.description = description;
    }

    public static Role fromDescription(String description) {
        return Arrays.stream(values())
                .filter(role -> role.description.equals(description))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("지원하지 않는 역할입니다: " + description));
    }
}
