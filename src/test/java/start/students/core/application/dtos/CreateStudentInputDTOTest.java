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
@DisplayName("CreateStudentInputDTO Tests")
class CreateStudentInputDTOTest {

    @Autowired
    private Validator validator;

    private CreateStudentInputDTO dto;

    @BeforeEach
    void setUp() {
        dto = new CreateStudentInputDTO();
    }

    @Test
    @DisplayName("Should validate valid student input")
    void shouldValidateValidStudentInput() {
        // ARRANGE
        dto.setName("João Silva Santos");
        dto.setCpf("11144477735");
        dto.setEmail("joao@gmail.com");
        dto.setPhone("11999999999");

        // ACT
        Set<ConstraintViolation<CreateStudentInputDTO>> violations = validator.validate(dto);

        // ASSERT
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Should reject null name")
    void shouldRejectNullName() {
        // ARRANGE
        dto.setName(null);
        dto.setCpf("12345678901");
        dto.setEmail("joao@gmail.com");
        dto.setPhone("11999999999");

        // ACT
        Set<ConstraintViolation<CreateStudentInputDTO>> violations = validator.validate(dto);

        // ASSERT
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getMessage().contains("Nome é obrigatório"));
    }

    @Test
    @DisplayName("Should reject name with less than 2 characters")
    void shouldRejectShortName() {
        // ARRANGE
        dto.setName("J");
        dto.setCpf("12345678901");
        dto.setEmail("joao@gmail.com");
        dto.setPhone("11999999999");

        // ACT
        Set<ConstraintViolation<CreateStudentInputDTO>> violations = validator.validate(dto);

        // ASSERT
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getMessage().contains("Nome deve ter entre 2 e 100 caracteres"));
    }

    @Test
    @DisplayName("Should reject name without full name")
    void shouldRejectNameWithoutFullName() {
        // ARRANGE
        dto.setName("João");
        dto.setCpf("12345678901");
        dto.setEmail("joao@gmail.com");
        dto.setPhone("11999999999");

        // ACT
        Set<ConstraintViolation<CreateStudentInputDTO>> violations = validator.validate(dto);

        // ASSERT
        assertThat(violations).isNotEmpty();
    }

    @Test
    @DisplayName("Should reject invalid CPF")
    void shouldRejectInvalidCPF() {
        // ARRANGE
        dto.setName("João Silva");
        dto.setCpf("invalid");
        dto.setEmail("joao@gmail.com");
        dto.setPhone("11999999999");

        // ACT
        Set<ConstraintViolation<CreateStudentInputDTO>> violations = validator.validate(dto);

        // ASSERT
        assertThat(violations).isNotEmpty();
    }

    @Test
    @DisplayName("Should reject email with accents")
    void shouldRejectEmailWithAccents() {
        // ARRANGE
        dto.setName("João Silva");
        dto.setCpf("12345678901");
        dto.setEmail("joão@gmail.com");
        dto.setPhone("11999999999");

        // ACT
        Set<ConstraintViolation<CreateStudentInputDTO>> violations = validator.validate(dto);

        // ASSERT
        assertThat(violations).isNotEmpty();
    }

    @Test
    @DisplayName("Should reject invalid phone")
    void shouldRejectInvalidPhone() {
        // ARRANGE
        dto.setName("João Silva");
        dto.setCpf("12345678901");
        dto.setEmail("joao@gmail.com");
        dto.setPhone("123");

        // ACT
        Set<ConstraintViolation<CreateStudentInputDTO>> violations = validator.validate(dto);

        // ASSERT
        assertThat(violations).isNotEmpty();
    }

    @Test
    @DisplayName("Should accept phone with 10 digits")
    void shouldAcceptPhoneWith10Digits() {
        // ARRANGE
        dto.setName("João Silva");
        dto.setCpf("11144477735");
        dto.setEmail("joao@gmail.com");
        dto.setPhone("1133334444");

        // ACT
        Set<ConstraintViolation<CreateStudentInputDTO>> violations = validator.validate(dto);

        // ASSERT
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Should accept phone with 11 digits")
    void shouldAcceptPhoneWith11Digits() {
        // ARRANGE
        dto.setName("João Silva");
        dto.setCpf("11144477735");
        dto.setEmail("joao@gmail.com");
        dto.setPhone("11999999999");

        // ACT
        Set<ConstraintViolation<CreateStudentInputDTO>> violations = validator.validate(dto);

        // ASSERT
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Should accept null photo")
    void shouldAcceptNullPhoto() {
        // ARRANGE
        dto.setName("João Silva");
        dto.setCpf("11144477735");
        dto.setEmail("joao@gmail.com");
        dto.setPhone("11999999999");
        dto.setPhoto(null);

        // ACT
        Set<ConstraintViolation<CreateStudentInputDTO>> violations = validator.validate(dto);

        // ASSERT
        assertThat(violations).isEmpty();
    }
}

