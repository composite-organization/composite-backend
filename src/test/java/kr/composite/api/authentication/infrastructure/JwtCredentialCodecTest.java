package kr.composite.api.authentication.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;
import kr.composite.api.authentication.domain.CredentialPayload;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JwtCredentialCodecTest {

    private JwtCredentialCodec jwtCredentialCodec;
    private final String secret = "vdu35oxV0ZEAeBI+JEA/9/Lx76d+0q52zD2xgvf1mbs=";
    private final Duration expiration = Duration.ofHours(1);

    @BeforeEach
    void setUp() {
        jwtCredentialCodec = new JwtCredentialCodec(secret, expiration);
    }

    @Test
    @DisplayName("페이로드를 JWT 토큰으로 인코딩하고 다시 디코딩하면 동일한 식별자가 반환된다.")
    void encodeAndDecode() {
        // given
        Long identifier = 1L;
        CredentialPayload payload = new CredentialPayload(identifier);

        // when
        String token = jwtCredentialCodec.encode(payload);
        CredentialPayload decodedPayload = jwtCredentialCodec.decode(token);

        // then
        assertThat(decodedPayload.identifier()).isEqualTo(identifier);
    }
}
