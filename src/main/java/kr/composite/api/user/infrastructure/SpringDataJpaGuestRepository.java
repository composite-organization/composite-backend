package kr.composite.api.user.infrastructure;

import java.util.Optional;
import kr.composite.api.user.domain.Guest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataJpaGuestRepository extends JpaRepository<Guest, Long> {

    Optional<Guest> findByUserId(Long userId);
}
