package start.students.core.domain.exceptions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("DomainException Tests")
class DomainExceptionTest {

    @Test
    @DisplayName("Should create DomainException with message")
    void shouldCreateDomainExceptionWithMessage() {
        // ARRANGE
        String message = "Test error message";

        // ACT
        DomainException exception = new DomainException(message);

        // ASSERT
        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isEqualTo(message);
        assertThat(exception).isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("Should create DomainException with message and cause")
    void shouldCreateDomainExceptionWithMessageAndCause() {
        // ARRANGE
        String message = "Test error message";
        Throwable cause = new RuntimeException("Root cause");

        // ACT
        DomainException exception = new DomainException(message, cause);

        // ASSERT
        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isEqualTo(message);
        assertThat(exception.getCause()).isEqualTo(cause);
    }

    @Test
    @DisplayName("Should create StudentNotFoundException which extends DomainException")
    void shouldCreateStudentNotFoundExceptionAsSubclass() {
        // ARRANGE
        String message = "Student not found";

        // ACT
        StudentNotFoundException exception = new StudentNotFoundException(message);

        // ASSERT
        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isEqualTo(message);
        assertThat(exception).isInstanceOf(DomainException.class);
        assertThat(exception).isInstanceOf(RuntimeException.class);
    }
}

