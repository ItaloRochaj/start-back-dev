package start.students.core.application.dtos;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@DisplayName("LoginInputDTO Tests")
class LoginInputDTOTest {

    @Autowired
    private Validator validator;

    private LoginInputDTO dto;

    @BeforeEach
    void setUp() {
        dto = new LoginInputDTO();
    }

    @Test
    @DisplayName("Should validate valid login input")
    void shouldValidateValidLoginInput() {
        // ARRANGE
        dto.setUsername("usertest123");
        dto.setPassword("password456");

        // ACT
        Set<ConstraintViolation<LoginInputDTO>> violations = validator.validate(dto);

        // ASSERT
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Should reject null username")
    void shouldRejectNullUsername() {
        // ARRANGE
        dto.setUsername(null);
        dto.setPassword("pass456");

        // ACT
        Set<ConstraintViolation<LoginInputDTO>> violations = validator.validate(dto);

        // ASSERT
        assertThat(violations).isNotEmpty();
    }

    @Test
    @DisplayName("Should reject short username")
    void shouldRejectShortUsername() {
        // ARRANGE
        dto.setUsername("usr123");
        dto.setPassword("pass456");

        // ACT
        Set<ConstraintViolation<LoginInputDTO>> violations = validator.validate(dto);

        // ASSERT
        assertThat(violations).isNotEmpty();
    }

    @Test
    @DisplayName("Should reject username with only letters")
    void shouldRejectUsernameWithOnlyLetters() {
        // ARRANGE
        dto.setUsername("onlyletters123");
        dto.setPassword("pass456");

        // ACT
        Set<ConstraintViolation<LoginInputDTO>> violations = validator.validate(dto);

        // ASSERT
        assertThat(violations).isNotEmpty();
    }

    @Test
    @DisplayName("Should reject username with special characters")
    void shouldRejectUsernameWithSpecialCharacters() {
        // ARRANGE
        dto.setUsername("user@123");
        dto.setPassword("pass456");

        // ACT
        Set<ConstraintViolation<LoginInputDTO>> violations = validator.validate(dto);

        // ASSERT
        assertThat(violations).isNotEmpty();
    }

    @Test
    @DisplayName("Should reject null password")
    void shouldRejectNullPassword() {
        // ARRANGE
        dto.setUsername("user123");
        dto.setPassword(null);

        // ACT
        Set<ConstraintViolation<LoginInputDTO>> violations = validator.validate(dto);

        // ASSERT
        assertThat(violations).isNotEmpty();
    }

    @Test
    @DisplayName("Should reject short password")
    void shouldRejectShortPassword() {
        // ARRANGE
        dto.setUsername("user123");
        dto.setPassword("pass12");

        // ACT
        Set<ConstraintViolation<LoginInputDTO>> violations = validator.validate(dto);

        // ASSERT
        assertThat(violations).isNotEmpty();
    }
}

