package kr.composite.api.user.infrastructure;

import java.util.Optional;
import kr.composite.api.user.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataJpaMemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByUserId(Long userId);
}
