package start.students.core.application.usecases;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import start.students.core.application.dtos.LoginInputDTO;
import start.students.core.application.dtos.TokenResponseDTO;
import start.students.core.domain.entities.User;
import start.students.core.domain.exceptions.DomainException;
import start.students.core.ports.JwtTokenPort;
import start.students.core.ports.PasswordEncoderPort;
import start.students.core.ports.UserRepositoryPort;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthenticationUseCase Tests")
class AuthenticationUseCaseTest {

    @Mock
    private UserRepositoryPort userRepository;

    @Mock
    private PasswordEncoderPort passwordEncoder;

    @Mock
    private JwtTokenPort jwtTokenPort;

    @InjectMocks
    private AuthenticationUseCase useCase;

    @Test
    @DisplayName("Should authenticate user successfully with valid credentials")
    void shouldAuthenticateUserSuccessfully() {
        // ARRANGE
        LoginInputDTO input = new LoginInputDTO();
        input.setUsername("user123");
        input.setPassword("pass456");

        User user = new User();
        user.setId("uuid-1");
        user.setUsername("user123");
        user.setPassword("encodedPassword");

        when(userRepository.findByUsername("user123")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("pass456", "encodedPassword")).thenReturn(true);
        when(jwtTokenPort.generateToken("user123")).thenReturn("jwt-token-123");

        // ACT
        TokenResponseDTO result = useCase.authenticate(input);

        // ASSERT
        assertThat(result).isNotNull();
        assertThat(result.getToken()).isEqualTo("jwt-token-123");
        assertThat(result.getUsername()).isEqualTo("user123");
        assertThat(result.getTokenType()).isEqualTo("Bearer");

        verify(userRepository, times(1)).findByUsername("user123");
        verify(passwordEncoder, times(1)).matches("pass456", "encodedPassword");
        verify(jwtTokenPort, times(1)).generateToken("user123");
    }

    @Test
    @DisplayName("Should throw exception when user not found")
    void shouldThrowExceptionWhenUserNotFound() {
        // ARRANGE
        LoginInputDTO input = new LoginInputDTO();
        input.setUsername("nonexistent1");
        input.setPassword("pass456");

        when(userRepository.findByUsername("nonexistent1")).thenReturn(Optional.empty());

        // ACT & ASSERT
        assertThatThrownBy(() -> useCase.authenticate(input))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Credenciais inválidas");

        verify(userRepository, times(1)).findByUsername("nonexistent1");
        verify(passwordEncoder, never()).matches(anyString(), anyString());
        verify(jwtTokenPort, never()).generateToken(anyString());
    }


    @Test
    @DisplayName("Should throw exception when username contains only letters")
    void shouldThrowExceptionWhenUsernameOnlyLetters() {
        // ARRANGE
        LoginInputDTO input = new LoginInputDTO();
        input.setUsername("onlyletters");
        input.setPassword("pass456");

        // ACT & ASSERT
        assertThatThrownBy(() -> useCase.authenticate(input))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Username e Password devem conter apenas letras (sem acento) e números");

        verify(userRepository, never()).findByUsername(anyString());
    }

    @Test
    @DisplayName("Should throw exception when password contains only numbers")
    void shouldThrowExceptionWhenPasswordOnlyNumbers() {
        // ARRANGE
        LoginInputDTO input = new LoginInputDTO();
        input.setUsername("user123");
        input.setPassword("123456");

        // ACT & ASSERT
        assertThatThrownBy(() -> useCase.authenticate(input))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Username e Password devem conter apenas letras (sem acento) e números");

        verify(userRepository, never()).findByUsername(anyString());
    }

    @Test
    @DisplayName("Should throw exception when username contains special characters")
    void shouldThrowExceptionWhenUsernameContainsSpecialCharacters() {
        // ARRANGE
        LoginInputDTO input = new LoginInputDTO();
        input.setUsername("user@123");
        input.setPassword("pass456");

        // ACT & ASSERT
        assertThatThrownBy(() -> useCase.authenticate(input))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Username e Password devem conter apenas letras (sem acento) e números");

        verify(userRepository, never()).findByUsername(anyString());
    }
}

