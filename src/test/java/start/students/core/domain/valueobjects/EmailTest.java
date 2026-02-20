package start.students.core.domain.valueobjects;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import start.students.core.domain.exceptions.DomainException;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Email Value Object Tests")
class EmailTest {

    @Test
    @DisplayName("should create valid email with supported domain")
    void shouldCreateValidEmailWithSupportedDomain() {
        // Arrange & Act
        Email email = new Email("user@gmail.com");

        // Assert
        assertThat(email.getValue())
                .isNotNull()
                .isEqualTo("user@gmail.com")
                .contains("@")
                .endsWith(".com");
    }

    @Test
    @DisplayName("should normalize email to lowercase")
    void shouldNormalizeEmailToLowercase() {
        // Arrange & Act
        Email email = new Email("User@Gmail.COM");

        // Assert
        assertThat(email.getValue())
                .isNotNull()
                .isLowerCase()
                .isEqualTo("user@gmail.com");
    }

    @Test
    @DisplayName("should create valid email from multiple supported domains")
    void shouldCreateValidEmailFromMultipleSupportedDomains() {
        // Arrange & Act & Assert
        assertThatNoException()
                .isThrownBy(() -> {
                    new Email("user@gmail.com");
                    new Email("user@outlook.com");
                    new Email("user@yahoo.com");
                    new Email("user@hotmail.com");
                });
    }

    @Test
    @DisplayName("should throw exception when email is null")
    void shouldThrowExceptionWhenEmailIsNull() {
        // Arrange & Act & Assert
        assertThatThrownBy(() -> new Email(null))
                .isInstanceOf(DomainException.class)
                .hasMessage("Email não pode ser vazio");
    }

    @Test
    @DisplayName("should throw exception when email is empty string")
    void shouldThrowExceptionWhenEmailIsEmpty() {
        // Arrange & Act & Assert
        assertThatThrownBy(() -> new Email(""))
                .isInstanceOf(DomainException.class)
                .hasMessage("Email não pode ser vazio");
    }

    @Test
    @DisplayName("should throw exception when email is blank string")
    void shouldThrowExceptionWhenEmailIsBlank() {
        // Arrange & Act & Assert
        assertThatThrownBy(() -> new Email("   "))
                .isInstanceOf(DomainException.class)
                .hasMessage("Email não pode ser vazio");
    }

    @Test
    @DisplayName("should throw exception when email lacks @ symbol")
    void shouldThrowExceptionWhenEmailLacksAtSymbol() {
        // Arrange & Act & Assert
        assertThatThrownBy(() -> new Email("usergmail.com"))
                .isInstanceOf(DomainException.class)
                .hasMessage("Email inválido ou provedor não suportado");
    }

    @Test
    @DisplayName("should throw exception when email lacks domain")
    void shouldThrowExceptionWhenEmailLacksDomain() {
        // Arrange & Act & Assert
        assertThatThrownBy(() -> new Email("user@"))
                .isInstanceOf(DomainException.class)
                .hasMessage("Email inválido ou provedor não suportado");
    }

    @Test
    @DisplayName("should throw exception when email has unsupported domain")
    void shouldThrowExceptionWhenEmailHasUnsupportedDomain() {
        // Arrange & Act & Assert
        assertThatThrownBy(() -> new Email("user@unsupported-domain-xyz.com"))
                .isInstanceOf(DomainException.class)
                .hasMessage("Email inválido ou provedor não suportado");
    }

    @Test
    @DisplayName("should throw exception when email contains accents")
    void shouldThrowExceptionWhenEmailContainsAccents() {
        // Arrange & Act & Assert
        assertThatThrownBy(() -> new Email("usér@gmail.com"))
                .isInstanceOf(DomainException.class)
                .hasMessage("Email inválido ou provedor não suportado");
    }

    @Test
    @DisplayName("should throw exception when email contains cedilla")
    void shouldThrowExceptionWhenEmailContainsCedilla() {
        // Arrange & Act & Assert
        assertThatThrownBy(() -> new Email("useç@gmail.com"))
                .isInstanceOf(DomainException.class)
                .hasMessage("Email inválido ou provedor não suportado");
    }

    @ParameterizedTest
    @ValueSource(strings = {"user@domain.c", "user@domain.", "user@.com", "user@domain@domain.com"})
    @DisplayName("should throw exception for invalid email formats")
    void shouldThrowExceptionForInvalidEmailFormats(String invalidEmail) {
        // Arrange & Act & Assert
        assertThatThrownBy(() -> new Email(invalidEmail))
                .isInstanceOf(DomainException.class)
                .hasMessage("Email inválido ou provedor não suportado");
    }

    @Test
    @DisplayName("should trim email before validation")
    void shouldTrimEmailBeforeValidation() {
        // Arrange & Act
        Email email = new Email("  user@gmail.com  ");

        // Assert
        assertThat(email.getValue())
                .isNotNull()
                .isEqualTo("user@gmail.com")
                .doesNotStartWith(" ")
                .doesNotEndWith(" ");
    }

    @Test
    @DisplayName("should handle email with special characters allowed in local part")
    void shouldHandleEmailWithSpecialCharactersInLocalPart() {
        // Arrange & Act & Assert
        assertThatNoException()
                .isThrownBy(() -> new Email("user.name+tag@gmail.com"));
    }

    @Test
    @DisplayName("should throw exception when email has multiple @ symbols")
    void shouldThrowExceptionWhenEmailHasMultipleAtSymbols() {
        // Arrange & Act & Assert
        assertThatThrownBy(() -> new Email("user@@gmail.com"))
                .isInstanceOf(DomainException.class)
                .hasMessage("Email inválido ou provedor não suportado");
    }

}

