package start.students.adapters.outbound.persistence.adapters;

import start.students.adapters.outbound.persistence.entities.UserJpaEntity;
import start.students.adapters.outbound.repositories.UserJpaRepository;
import start.students.core.domain.entities.User;
import start.students.core.ports.UserRepositoryPort;

import java.util.Optional;

public class UserPersistenceAdapter implements UserRepositoryPort {

    private final UserJpaRepository repository;

    public UserPersistenceAdapter(UserJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return repository.findByUsername(username).map(this::toDomain);
    }

    @Override
    public User save(User user) {
        UserJpaEntity entity = toEntity(user);
        UserJpaEntity savedEntity = repository.save(entity);
        return toDomain(savedEntity);
    }

    private UserJpaEntity toEntity(User user) {
        return new UserJpaEntity(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getEmail(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }

    private User toDomain(UserJpaEntity entity) {
        return new User(
                entity.getId(),
                entity.getUsername(),
                entity.getPassword(),
                entity.getEmail(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
