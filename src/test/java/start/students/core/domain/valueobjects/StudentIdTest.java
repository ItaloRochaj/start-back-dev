package start.students.core.domain.valueobjects;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import start.students.core.domain.exceptions.DomainException;

import static org.assertj.core.api.Assertions.*;

@DisplayName("StudentId Value Object Tests")
class StudentIdTest {

    @Test
    @DisplayName("should create StudentId with valid UUID string")
    void shouldCreateStudentIdWithValidUuidString() {
        // Arrange
        String validUuid = "550e8400-e29b-41d4-a716-446655440000";

        // Act
        StudentId studentId = new StudentId(validUuid);

        // Assert
        assertThat(studentId.getValue())
                .isNotNull()
                .isEqualTo(validUuid);
    }

    @Test
    @DisplayName("should create StudentId with any non-empty string")
    void shouldCreateStudentIdWithAnyNonEmptyString() {
        // Arrange
        String customId = "STUDENT-001";

        // Act
        StudentId studentId = new StudentId(customId);

        // Assert
        assertThat(studentId.getValue())
                .isNotNull()
                .isEqualTo(customId);
    }

    @Test
    @DisplayName("should throw exception when StudentId is null")
    void shouldThrowExceptionWhenStudentIdIsNull() {
        // Arrange & Act & Assert
        assertThatThrownBy(() -> new StudentId(null))
                .isInstanceOf(DomainException.class)
                .hasMessage("ID do estudante não pode ser vazio");
    }

    @Test
    @DisplayName("should throw exception when StudentId is empty string")
    void shouldThrowExceptionWhenStudentIdIsEmpty() {
        // Arrange & Act & Assert
        assertThatThrownBy(() -> new StudentId(""))
                .isInstanceOf(DomainException.class)
                .hasMessage("ID do estudante não pode ser vazio");
    }

    @Test
    @DisplayName("should throw exception when StudentId is blank string")
    void shouldThrowExceptionWhenStudentIdIsBlank() {
        // Arrange & Act & Assert
        assertThatThrownBy(() -> new StudentId("   "))
                .isInstanceOf(DomainException.class)
                .hasMessage("ID do estudante não pode ser vazio");
    }

    @Test
    @DisplayName("should trim StudentId before storing")
    void shouldTrimStudentIdBeforeStoring() {
        // Arrange & Act
        StudentId studentId = new StudentId("  some-id  ");

        // Assert
        assertThat(studentId.getValue())
                .isNotNull()
                .isEqualTo("some-id")
                .doesNotStartWith(" ")
                .doesNotEndWith(" ");
    }

    @Test
    @DisplayName("should generate unique StudentId")
    void shouldGenerateUniqueStudentId() {
        // Arrange & Act
        StudentId studentId1 = StudentId.generate();
        StudentId studentId2 = StudentId.generate();

        // Assert
        assertThat(studentId1.getValue())
                .isNotNull()
                .isNotEmpty();

        assertThat(studentId2.getValue())
                .isNotNull()
                .isNotEmpty();

        assertThat(studentId1.getValue())
                .isNotEqualTo(studentId2.getValue());
    }

    @Test
    @DisplayName("should generate StudentId with valid UUID format")
    void shouldGenerateStudentIdWithValidUuidFormat() {
        // Arrange & Act
        StudentId studentId = StudentId.generate();

        // Assert
        assertThat(studentId.getValue())
                .isNotNull()
                .matches("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}");
    }

    @Test
    @DisplayName("should have correct value equality")
    void shouldHaveCorrectValueEquality() {
        // Arrange
        String id = "test-id-123";
        StudentId studentId1 = new StudentId(id);
        StudentId studentId2 = new StudentId(id);

        // Act & Assert
        assertThat(studentId1).isEqualTo(studentId2);
    }

    @Test
    @DisplayName("should handle StudentId with special characters")
    void shouldHandleStudentIdWithSpecialCharacters() {
        // Arrange
        String specialId = "student-id-@#$%";

        // Act
        StudentId studentId = new StudentId(specialId);

        // Assert
        assertThat(studentId.getValue())
                .isNotNull()
                .isEqualTo(specialId);
    }

    @Test
    @DisplayName("should handle StudentId with numbers only")
    void shouldHandleStudentIdWithNumbersOnly() {
        // Arrange
        String numericId = "123456789";

        // Act
        StudentId studentId = new StudentId(numericId);

        // Assert
        assertThat(studentId.getValue())
                .isNotNull()
                .isEqualTo(numericId)
                .matches("\\d+");
    }

}

