package kr.composite.api.user.ui;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;

class CredentialTranslatorTest {

    private CredentialTranslator credentialTranslator;

    @BeforeEach
    void setUp() {
        credentialTranslator = new CredentialTranslator();
    }

    @Test
    @DisplayName("요청 헤더에 Bearer 토큰이 있으면 이를 추출한다.")
    void extract() {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        String token = "test.jwt.token";
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer " + token);

        // when
        Optional<String> extractedToken = credentialTranslator.extract(request);

        // then
        assertThat(extractedToken).isPresent().contains(token);
    }

    @Test
    @DisplayName("응답 헤더에 Credential을 주입한다.")
    void inject() {
        // given
        HttpServletResponse response = mock(HttpServletResponse.class);
        String token = "test.jwt.token";

        // when
        credentialTranslator.inject(response, token);

        // then
        verify(response).setHeader(HttpHeaders.AUTHORIZATION, token);
    }
}
