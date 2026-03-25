package kr.composite.api.user.infrastructure;

import kr.composite.api.user.domain.Member;
import kr.composite.api.user.domain.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaMemberRepository implements MemberRepository {

    private final SpringDataJpaMemberRepository springDataJpaMemberRepository;

    @Override
    public Member save(Member member) {
        return springDataJpaMemberRepository.save(member);
    }

    @Override
    public Optional<Member> findByUserId(Long userId) {
        return springDataJpaMemberRepository.findByUserId(userId);
    }
}
