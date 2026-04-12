package kr.composite.api.user.infrastructure;

import kr.composite.api.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataJpaUserRepository extends JpaRepository<User, Long> {

}
