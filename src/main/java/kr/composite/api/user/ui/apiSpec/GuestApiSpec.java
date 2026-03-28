package kr.composite.api.user.ui.apiSpec;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import kr.composite.api.user.ui.dto.request.CreateGuestCredentialRequest;
import org.springframework.http.ResponseEntity;

@Tag(name = "게스트 API", description = "게스트 관련 API 명세입니다.")
public interface GuestApiSpec {

    @Operation(summary = "게스트 인증 정보 생성", description = "게스트 이름을 입력받아 새로운 게스트를 생성하고 인증 토큰을 발급합니다.")
    ResponseEntity<Void> createCredentials(
            HttpServletResponse response,
            CreateGuestCredentialRequest request
    );
}
