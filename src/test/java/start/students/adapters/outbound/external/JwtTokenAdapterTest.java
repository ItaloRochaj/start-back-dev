package start.students.adapters.outbound.external;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import javax.crypto.SecretKey;
import java.util.Date;

import static org.assertj.core.api.Assertions.*;

@DisplayName("JwtTokenAdapter Tests")
class JwtTokenAdapterTest {

    private JwtTokenAdapter adapter;
    private String secretKey;
    private Long expiration;

    @BeforeEach
    void setUp() {
        adapter = new JwtTokenAdapter();
        secretKey = "mySecretKey123456789012345678901234567890";
        expiration = 86400000L; // 24 hours

        ReflectionTestUtils.setField(adapter, "secretKey", secretKey);
        ReflectionTestUtils.setField(adapter, "expiration", expiration);
    }

    @Test
    @DisplayName("Should generate token successfully")
    void shouldGenerateTokenSuccessfully() {
        // ARRANGE
        String username = "user123";

        // ACT
        String token = adapter.generateToken(username);

        // ASSERT
        assertThat(token).isNotNull();
        assertThat(token).isNotEmpty();
        assertThat(token).contains(".");
    }

    @Test
    @DisplayName("Should extract username from token")
    void shouldExtractUsernameFromToken() {
        // ARRANGE
        String username = "user123";
        String token = adapter.generateToken(username);

        // ACT
        String extractedUsername = adapter.extractUsername(token);

        // ASSERT
        assertThat(extractedUsername).isEqualTo(username);
    }

    @Test
    @DisplayName("Should validate token successfully")
    void shouldValidateTokenSuccessfully() {
        // ARRANGE
        String username = "user123";
        String token = adapter.generateToken(username);

        // ACT
        boolean isValid = adapter.isTokenValid(token, username);

        // ASSERT
        assertThat(isValid).isTrue();
    }

    @Test
    @DisplayName("Should return false when token username does not match")
    void shouldReturnFalseWhenTokenUsernameDoesNotMatch() {
        // ARRANGE
        String username = "user123";
        String token = adapter.generateToken(username);

        // ACT
        boolean isValid = adapter.isTokenValid(token, "differentUser");

        // ASSERT
        assertThat(isValid).isFalse();
    }


    @Test
    @DisplayName("Should generate valid token with special characters in username")
    void shouldGenerateValidTokenWithSpecialCharactersInUsername() {
        // ARRANGE
        String username = "user_123@example";

        // ACT
        String token = adapter.generateToken(username);
        String extractedUsername = adapter.extractUsername(token);

        // ASSERT
        assertThat(extractedUsername).isEqualTo(username);
    }
}

