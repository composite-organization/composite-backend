package kr.composite.api.user.infrastructure;

import java.util.Optional;
import kr.composite.api.user.domain.Guest;
import kr.composite.api.user.domain.GuestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JpaGuestRepository implements GuestRepository {

    private final SpringDataJpaGuestRepository springDataJpaGuestRepository;

    @Override
    public Guest save(Guest guest) {
        return springDataJpaGuestRepository.save(guest);
    }

    @Override
    public Optional<Guest> findByUserId(Long userId) {
        return springDataJpaGuestRepository.findByUserId(userId);
    }
}
