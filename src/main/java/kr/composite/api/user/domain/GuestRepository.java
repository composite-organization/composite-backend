package kr.composite.api.user.domain;

import java.util.Optional;

public interface GuestRepository {

    Guest save(Guest guest);

    Optional<Guest> findByUserId(Long userId);
}
