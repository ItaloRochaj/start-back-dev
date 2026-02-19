package start.students.adapters.outbound.external;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("PasswordEncoderAdapter Tests")
class PasswordEncoderAdapterTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private PasswordEncoderAdapter adapter;

    @Test
    @DisplayName("Should encode password successfully")
    void shouldEncodePasswordSuccessfully() {
        // ARRANGE
        String rawPassword = "password123";
        String encodedPassword = "$2a$10$encodedPasswordHash";

        when(passwordEncoder.encode(rawPassword)).thenReturn(encodedPassword);

        // ACT
        String result = adapter.encode(rawPassword);

        // ASSERT
        assertThat(result).isEqualTo(encodedPassword);
        verify(passwordEncoder, times(1)).encode(rawPassword);
    }

    @Test
    @DisplayName("Should verify password matches successfully")
    void shouldVerifyPasswordMatches() {
        // ARRANGE
        String rawPassword = "password123";
        String encodedPassword = "$2a$10$encodedPasswordHash";

        when(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(true);

        // ACT
        boolean result = adapter.matches(rawPassword, encodedPassword);

        // ASSERT
        assertThat(result).isTrue();
        verify(passwordEncoder, times(1)).matches(rawPassword, encodedPassword);
    }

    @Test
    @DisplayName("Should return false when password does not match")
    void shouldReturnFalseWhenPasswordDoesNotMatch() {
        // ARRANGE
        String rawPassword = "password123";
        String wrongPassword = "wrongpassword";
        String encodedPassword = "$2a$10$encodedPasswordHash";

        when(passwordEncoder.matches(wrongPassword, encodedPassword)).thenReturn(false);

        // ACT
        boolean result = adapter.matches(wrongPassword, encodedPassword);

        // ASSERT
        assertThat(result).isFalse();
        verify(passwordEncoder, times(1)).matches(wrongPassword, encodedPassword);
    }

    @Test
    @DisplayName("Should delegate encode to underlying PasswordEncoder")
    void shouldDelegateEncodeToUnderlyingPasswordEncoder() {
        // ARRANGE
        String rawPassword = "test123";

        when(passwordEncoder.encode(rawPassword)).thenReturn("encoded");

        // ACT
        adapter.encode(rawPassword);

        // ASSERT
        verify(passwordEncoder).encode(rawPassword);
    }

    @Test
    @DisplayName("Should delegate matches to underlying PasswordEncoder")
    void shouldDelegateMatchesToUnderlyingPasswordEncoder() {
        // ARRANGE
        String rawPassword = "test123";
        String encodedPassword = "encoded";

        when(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(true);

        // ACT
        adapter.matches(rawPassword, encodedPassword);

        // ASSERT
        verify(passwordEncoder).matches(rawPassword, encodedPassword);
    }
}

