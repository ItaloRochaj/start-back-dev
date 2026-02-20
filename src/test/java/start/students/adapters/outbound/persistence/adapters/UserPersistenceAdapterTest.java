package start.students.adapters.outbound.persistence.adapters;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import start.students.adapters.outbound.persistence.entities.UserJpaEntity;
import start.students.adapters.outbound.repositories.UserJpaRepository;
import start.students.core.domain.entities.User;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@DisplayName("UserPersistenceAdapter Tests")
class UserPersistenceAdapterTest {

    @Mock
    private UserJpaRepository repository;

    @InjectMocks
    private UserPersistenceAdapter adapter;

    private UserJpaEntity userEntity;
    private User userDomain;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        LocalDateTime now = LocalDateTime.now();

        userEntity = new UserJpaEntity(
                "550e8400-e29b-41d4-a716-446655440000",
                "user123",
                "hashedPassword",
                "user@gmail.com",
                now,
                now
        );

        userDomain = new User(
                "550e8400-e29b-41d4-a716-446655440000",
                "user123",
                "hashedPassword",
                "user@gmail.com",
                now,
                now
        );
    }

    @Test
    @DisplayName("should save user successfully")
    void shouldSaveUserSuccessfully() {
        // Arrange
        when(repository.save(any(UserJpaEntity.class))).thenReturn(userEntity);

        // Act
        User savedUser = adapter.save(userDomain);

        // Assert
        assertThat(savedUser)
                .isNotNull()
                .hasFieldOrPropertyWithValue("username", "user123")
                .hasFieldOrPropertyWithValue("email", "user@gmail.com");

        verify(repository, times(1)).save(any(UserJpaEntity.class));
    }

    @Test
    @DisplayName("should find user by username successfully")
    void shouldFindUserByUsernameSuccessfully() {
        // Arrange
        String username = "user123";
        when(repository.findByUsername(username)).thenReturn(Optional.of(userEntity));

        // Act
        Optional<User> foundUser = adapter.findByUsername(username);

        // Assert
        assertThat(foundUser)
                .isPresent()
                .hasValueSatisfying(user -> assertThat(user.getUsername()).isEqualTo("user123"));

        verify(repository, times(1)).findByUsername(username);
    }

    @Test
    @DisplayName("should return empty when user not found by username")
    void shouldReturnEmptyWhenUserNotFoundByUsername() {
        // Arrange
        String nonExistentUsername = "nonexistent";
        when(repository.findByUsername(nonExistentUsername)).thenReturn(Optional.empty());

        // Act
        Optional<User> foundUser = adapter.findByUsername(nonExistentUsername);

        // Assert
        assertThat(foundUser).isEmpty();

        verify(repository, times(1)).findByUsername(nonExistentUsername);
    }

    @Test
    @DisplayName("should map entity to domain correctly on save")
    void shouldMapEntityToDomainCorrectlyOnSave() {
        // Arrange
        when(repository.save(any(UserJpaEntity.class))).thenReturn(userEntity);

        // Act
        User result = adapter.save(userDomain);

        // Assert
        assertThat(result)
                .isNotNull()
                .hasFieldOrPropertyWithValue("username", userEntity.getUsername())
                .hasFieldOrPropertyWithValue("email", userEntity.getEmail())
                .hasFieldOrPropertyWithValue("password", userEntity.getPassword());
    }

    @Test
    @DisplayName("should map entity to domain correctly on findByUsername")
    void shouldMapEntityToDomainCorrectlyOnFindByUsername() {
        // Arrange
        String username = "user123";
        when(repository.findByUsername(username)).thenReturn(Optional.of(userEntity));

        // Act
        Optional<User> result = adapter.findByUsername(username);

        // Assert
        assertThat(result)
                .isPresent()
                .hasValueSatisfying(user -> {
                    assertThat(user.getId()).isEqualTo(userEntity.getId());
                    assertThat(user.getUsername()).isEqualTo(userEntity.getUsername());
                    assertThat(user.getEmail()).isEqualTo(userEntity.getEmail());
                });
    }

}
