package kr.composite.api.user.infrastructure;

import java.util.Optional;
import kr.composite.api.user.domain.User;
import kr.composite.api.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JpaUserRepository implements UserRepository {

    private final SpringDataJpaUserRepository springDataJpaUserRepository;

    @Override
    public User save(User user) {
        return springDataJpaUserRepository.save(user);
    }

    @Override
    public Optional<User> findById(Long id) {
        return springDataJpaUserRepository.findById(id);
    }
}
