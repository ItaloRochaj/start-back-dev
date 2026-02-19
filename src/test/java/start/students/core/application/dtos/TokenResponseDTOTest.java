package start.students.core.application.dtos;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("TokenResponseDTO Tests")
class TokenResponseDTOTest {

    @Test
    @DisplayName("Should build TokenResponseDTO successfully")
    void shouldBuildTokenResponseDTOSuccessfully() {
        // ARRANGE & ACT
        TokenResponseDTO dto = TokenResponseDTO.builder()
                .token("jwt-token-123")
                .username("user123")
                .build();

        // ASSERT
        assertThat(dto).isNotNull();
        assertThat(dto.getToken()).isEqualTo("jwt-token-123");
        assertThat(dto.getUsername()).isEqualTo("user123");
        assertThat(dto.getTokenType()).isEqualTo("Bearer");
    }

    @Test
    @DisplayName("Should build TokenResponseDTO with custom token type")
    void shouldBuildTokenResponseDTOWithCustomTokenType() {
        // ARRANGE & ACT
        TokenResponseDTO dto = TokenResponseDTO.builder()
                .token("jwt-token-123")
                .username("user123")
                .tokenType("Bearer")
                .build();

        // ASSERT
        assertThat(dto.getTokenType()).isEqualTo("Bearer");
    }

    @Test
    @DisplayName("Should have default token type")
    void shouldHaveDefaultTokenType() {
        // ARRANGE & ACT
        TokenResponseDTO dto = TokenResponseDTO.builder()
                .token("jwt-token-123")
                .username("user123")
                .build();

        // ASSERT
        assertThat(dto.getTokenType()).isEqualTo("Bearer");
    }

    @Test
    @DisplayName("Should allow setting all fields")
    void shouldAllowSettingAllFields() {
        // ARRANGE & ACT
        TokenResponseDTO dto = TokenResponseDTO.builder()
                .token("new-token")
                .username("newuser")
                .tokenType("CustomType")
                .build();

        // ASSERT
        assertThat(dto.getToken()).isEqualTo("new-token");
        assertThat(dto.getUsername()).isEqualTo("newuser");
        assertThat(dto.getTokenType()).isEqualTo("CustomType");
    }
}

