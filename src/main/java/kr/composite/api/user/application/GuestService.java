package kr.composite.api.user.application;

import kr.composite.api.authentication.domain.CredentialCodec;
import kr.composite.api.authentication.domain.CredentialPayload;
import kr.composite.api.user.domain.Guest;
import kr.composite.api.user.domain.GuestRepository;
import kr.composite.api.user.domain.User;
import kr.composite.api.user.domain.UserName;
import kr.composite.api.user.domain.UserRepository;
import kr.composite.api.user.ui.dto.request.CreateGuestCredentialRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GuestService {

    private final UserRepository userRepository;
    private final GuestRepository guestRepository;
    private final CredentialCodec credentialCodec;

    @Transactional
    public String addGuestCredential(CreateGuestCredentialRequest request) {
        User user = new User(new UserName(request.name()));
        User savedUser = userRepository.save(user);

        Guest guest = new Guest(savedUser.getId());
        guestRepository.save(guest);

        CredentialPayload payload = new CredentialPayload(savedUser.getId());

        return credentialCodec.encode(payload);
    }
}
