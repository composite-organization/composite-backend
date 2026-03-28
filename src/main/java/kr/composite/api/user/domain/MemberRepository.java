package kr.composite.api.user.domain;

import java.util.Optional;

public interface MemberRepository {

    Member save(Member member);

    Optional<Member> findByUserId(Long userId);
}
