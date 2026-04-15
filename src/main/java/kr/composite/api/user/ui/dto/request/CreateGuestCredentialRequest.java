package kr.composite.api.user.ui.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "게스트 인증 정보 생성 요청")
public record CreateGuestCredentialRequest(
        @NotNull
        @Schema(description = "게스트 이름", example = "홍길동")
        String name
) {

}
