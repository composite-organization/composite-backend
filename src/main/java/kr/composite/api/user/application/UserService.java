package kr.composite.api.user.application;

import kr.composite.api.user.domain.Guest;
import kr.composite.api.user.domain.GuestRepository;
import kr.composite.api.user.domain.Member;
import kr.composite.api.user.domain.MemberRepository;
import kr.composite.api.user.domain.User;
import kr.composite.api.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final MemberRepository memberRepository;
    private final GuestRepository guestRepository;

    public User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(UserApplicationException::userNotFound);
    }

    public Member getMember(User user) {
        return memberRepository.findByUserId(user.getId())
                .orElseThrow(UserApplicationException::memberNotFound);
    }

    public Guest getGuest(User user) {
        return guestRepository.findByUserId(user.getId())
                .orElseThrow(UserApplicationException::guestNotFound);
    }
}
