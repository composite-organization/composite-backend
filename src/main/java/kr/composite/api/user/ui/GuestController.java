package kr.composite.api.user.ui;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import kr.composite.api.user.application.GuestService;
import kr.composite.api.user.ui.apiSpec.GuestApiSpec;
import kr.composite.api.user.ui.dto.request.PostGuestCredentialRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/guests")
public class GuestController implements GuestApiSpec {

    private final GuestService guestService;
    private final CredentialTranslator credentialTranslator;

    @Override
    @PostMapping("/credentials")
    public ResponseEntity<Void> createCredentials(
            HttpServletResponse response,
            @Valid @RequestBody PostGuestCredentialRequest request
    ) {
        String token = guestService.createGuestCredential(request);
        credentialTranslator.inject(response, token);

        return ResponseEntity.ok().build();
    }
}

