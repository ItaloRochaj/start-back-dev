package start.students.core.domain.valueobjects;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import start.students.core.domain.exceptions.DomainException;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Status Value Object Tests")
class StatusTest {

    @Test
    @DisplayName("should create ACTIVE status")
    void shouldCreateActiveStatus() {
        // Arrange & Act
        Status status = new Status("ACTIVE");

        // Assert
        assertThat(status.getValue())
                .isNotNull()
                .isEqualTo("ACTIVE");
    }

    @Test
    @DisplayName("should create INACTIVE status")
    void shouldCreateInactiveStatus() {
        // Arrange & Act
        Status status = new Status("INACTIVE");

        // Assert
        assertThat(status.getValue())
                .isNotNull()
                .isEqualTo("INACTIVE");
    }

    @Test
    @DisplayName("should create SUSPENDED status")
    void shouldCreateSuspendedStatus() {
        // Arrange & Act
        Status status = new Status("SUSPENDED");

        // Assert
        assertThat(status.getValue())
                .isNotNull()
                .isEqualTo("SUSPENDED");
    }

    @Test
    @DisplayName("should normalize status to uppercase")
    void shouldNormalizeStatusToUppercase() {
        // Arrange & Act
        Status status = new Status("active");

        // Assert
        assertThat(status.getValue())
                .isNotNull()
                .isUpperCase()
                .isEqualTo("ACTIVE");
    }

    @Test
    @DisplayName("should normalize mixed case status to uppercase")
    void shouldNormalizeMixedCaseStatusToUppercase() {
        // Arrange & Act
        Status status = new Status("InAcTiVe");

        // Assert
        assertThat(status.getValue())
                .isNotNull()
                .isUpperCase()
                .isEqualTo("INACTIVE");
    }

    @Test
    @DisplayName("should throw exception when status is null")
    void shouldThrowExceptionWhenStatusIsNull() {
        // Arrange & Act & Assert
        assertThatThrownBy(() -> new Status(null))
                .isInstanceOf(DomainException.class)
                .hasMessage("Status não pode ser vazio");
    }

    @Test
    @DisplayName("should throw exception when status is empty string")
    void shouldThrowExceptionWhenStatusIsEmpty() {
        // Arrange & Act & Assert
        assertThatThrownBy(() -> new Status(""))
                .isInstanceOf(DomainException.class)
                .hasMessage("Status não pode ser vazio");
    }

    @Test
    @DisplayName("should throw exception when status is blank string")
    void shouldThrowExceptionWhenStatusIsBlank() {
        // Arrange & Act & Assert
        assertThatThrownBy(() -> new Status("   "))
                .isInstanceOf(DomainException.class)
                .hasMessage("Status não pode ser vazio");
    }

    @ParameterizedTest
    @ValueSource(strings = {"PENDING", "DELETED", "ARCHIVED", "UNKNOWN", "DISABLED"})
    @DisplayName("should throw exception for invalid status values")
    void shouldThrowExceptionForInvalidStatusValues(String invalidStatus) {
        // Arrange & Act & Assert
        assertThatThrownBy(() -> new Status(invalidStatus))
                .isInstanceOf(DomainException.class)
                .hasMessage("Status inválido. Valores aceitos: ACTIVE, INACTIVE, SUSPENDED");
    }

    @Test
    @DisplayName("should trim status before validation")
    void shouldTrimStatusBeforeValidation() {
        // Arrange & Act
        Status status = new Status("  ACTIVE  ");

        // Assert
        assertThat(status.getValue())
                .isNotNull()
                .isEqualTo("ACTIVE")
                .doesNotStartWith(" ")
                .doesNotEndWith(" ");
    }

    @Test
    @DisplayName("should create status using active factory method")
    void shouldCreateStatusUsingActiveFactoryMethod() {
        // Arrange & Act
        Status status = Status.active();

        // Assert
        assertThat(status.getValue())
                .isNotNull()
                .isEqualTo("ACTIVE");
    }

    @Test
    @DisplayName("should create status using inactive factory method")
    void shouldCreateStatusUsingInactiveFactoryMethod() {
        // Arrange & Act
        Status status = Status.inactive();

        // Assert
        assertThat(status.getValue())
                .isNotNull()
                .isEqualTo("INACTIVE");
    }

    @Test
    @DisplayName("should create status using suspended factory method")
    void shouldCreateStatusUsingSuspendedFactoryMethod() {
        // Arrange & Act
        Status status = Status.suspended();

        // Assert
        assertThat(status.getValue())
                .isNotNull()
                .isEqualTo("SUSPENDED");
    }

    @Test
    @DisplayName("should have correct status constants")
    void shouldHaveCorrectStatusConstants() {
        // Arrange & Act & Assert
        assertThat(Status.ACTIVE).isEqualTo("ACTIVE");
        assertThat(Status.INACTIVE).isEqualTo("INACTIVE");
        assertThat(Status.SUSPENDED).isEqualTo("SUSPENDED");
    }

    @Test
    @DisplayName("should have correct value equality for same status")
    void shouldHaveCorrectValueEqualityForSameStatus() {
        // Arrange
        Status status1 = new Status("ACTIVE");
        Status status2 = new Status("ACTIVE");

        // Act & Assert
        assertThat(status1).isEqualTo(status2);
    }

    @Test
    @DisplayName("should have different values for different statuses")
    void shouldHaveDifferentValuesForDifferentStatuses() {
        // Arrange
        Status status1 = new Status("ACTIVE");
        Status status2 = new Status("INACTIVE");

        // Act & Assert
        assertThat(status1.getValue())
                .isNotEqualTo(status2.getValue());
    }

}

