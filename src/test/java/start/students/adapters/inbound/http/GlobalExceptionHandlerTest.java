package start.students.adapters.inbound.http;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import start.students.core.domain.exceptions.DomainException;
import start.students.core.domain.exceptions.StudentNotFoundException;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("GlobalExceptionHandler Tests")
class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler handler;

    @Test
    @DisplayName("Should handle DomainException")
    void shouldHandleDomainException() {
        // ARRANGE
        DomainException exception = new DomainException("Test domain error");

        // ACT
        ResponseEntity<ApiResponse<Void>> response = handler.handleDomainException(exception);

        // ASSERT
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().isSuccess()).isFalse();
        assertThat(response.getBody().getMessage()).isEqualTo("Test domain error");
    }

    @Test
    @DisplayName("Should handle StudentNotFoundException")
    void shouldHandleStudentNotFoundException() {
        // ARRANGE
        StudentNotFoundException exception = new StudentNotFoundException("Student not found");

        // ACT
        ResponseEntity<ApiResponse<Void>> response = handler.handleDomainException(exception);

        // ASSERT
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().isSuccess()).isFalse();
    }

    @Test
    @DisplayName("Should handle DataIntegrityViolationException with CPF constraint")
    void shouldHandleDataIntegrityViolationExceptionWithCPF() {
        // ARRANGE
        DataIntegrityViolationException exception =
            new DataIntegrityViolationException("Violação de constraint: cpf_unique");

        // ACT
        ResponseEntity<ApiResponse<Void>> response = handler.handleDataIntegrityViolation(exception);

        // ASSERT
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().isSuccess()).isFalse();
        assertThat(response.getBody().getMessage().toLowerCase()).contains("cpf");
    }

    @Test
    @DisplayName("Should handle DataIntegrityViolationException with Email constraint")
    void shouldHandleDataIntegrityViolationExceptionWithEmail() {
        // ARRANGE
        DataIntegrityViolationException exception =
            new DataIntegrityViolationException("Violação de constraint: email_unique");

        // ACT
        ResponseEntity<ApiResponse<Void>> response = handler.handleDataIntegrityViolation(exception);

        // ASSERT
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().isSuccess()).isFalse();
        assertThat(response.getBody().getMessage().toLowerCase()).contains("email");
    }

    @Test
    @DisplayName("Should handle DataIntegrityViolationException with generic message")
    void shouldHandleDataIntegrityViolationExceptionWithGenericMessage() {
        // ARRANGE
        DataIntegrityViolationException exception =
            new DataIntegrityViolationException("Constraint violation");

        // ACT
        ResponseEntity<ApiResponse<Void>> response = handler.handleDataIntegrityViolation(exception);

        // ASSERT
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().isSuccess()).isFalse();
        assertThat(response.getBody().getMessage().toLowerCase()).contains("dados duplicados");
    }

    @Test
    @DisplayName("Should handle MethodArgumentNotValidException with username pattern error")
    void shouldHandleMethodArgumentNotValidExceptionWithUsernamePattern() {
        // ARRANGE
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError("object", "username", "pattern");
        List<FieldError> fieldErrors = new ArrayList<>();
        fieldErrors.add(fieldError);

        when(bindingResult.getFieldErrors()).thenReturn(fieldErrors);

        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        when(exception.getBindingResult()).thenReturn(bindingResult);

        // ACT
        ResponseEntity<ApiResponse<Void>> response = handler.handleValidationException(exception);

        // ASSERT
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().isSuccess()).isFalse();
        assertThat(response.getBody().getMessage()).contains("Username deve conter apenas letras");
    }

    @Test
    @DisplayName("Should handle MethodArgumentNotValidException with username size error")
    void shouldHandleMethodArgumentNotValidExceptionWithUsernameSize() {
        // ARRANGE
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError("object", "username", "size");
        List<FieldError> fieldErrors = new ArrayList<>();
        fieldErrors.add(fieldError);

        when(bindingResult.getFieldErrors()).thenReturn(fieldErrors);

        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        when(exception.getBindingResult()).thenReturn(bindingResult);

        // ACT
        ResponseEntity<ApiResponse<Void>> response = handler.handleValidationException(exception);

        // ASSERT
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).contains("Username deve conter entre 8 e 50");
    }

    @Test
    @DisplayName("Should handle MethodArgumentNotValidException with password pattern error")
    void shouldHandleMethodArgumentNotValidExceptionWithPasswordPattern() {
        // ARRANGE
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError("object", "password", "pattern");
        List<FieldError> fieldErrors = new ArrayList<>();
        fieldErrors.add(fieldError);

        when(bindingResult.getFieldErrors()).thenReturn(fieldErrors);

        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        when(exception.getBindingResult()).thenReturn(bindingResult);

        // ACT
        ResponseEntity<ApiResponse<Void>> response = handler.handleValidationException(exception);

        // ASSERT
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().getMessage()).contains("Password deve conter apenas letras");
    }

    @Test
    @DisplayName("Should handle MethodArgumentNotValidException with password size error")
    void shouldHandleMethodArgumentNotValidExceptionWithPasswordSize() {
        // ARRANGE
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError("object", "password", "size");
        List<FieldError> fieldErrors = new ArrayList<>();
        fieldErrors.add(fieldError);

        when(bindingResult.getFieldErrors()).thenReturn(fieldErrors);

        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        when(exception.getBindingResult()).thenReturn(bindingResult);

        // ACT
        ResponseEntity<ApiResponse<Void>> response = handler.handleValidationException(exception);

        // ASSERT
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().getMessage()).contains("Password deve conter entre 8 e 100");
    }

    @Test
    @DisplayName("Should handle MethodArgumentNotValidException with generic field error")
    void shouldHandleMethodArgumentNotValidExceptionWithGenericField() {
        // ARRANGE
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError("object", "someField", "is invalid");
        List<FieldError> fieldErrors = new ArrayList<>();
        fieldErrors.add(fieldError);

        when(bindingResult.getFieldErrors()).thenReturn(fieldErrors);

        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        when(exception.getBindingResult()).thenReturn(bindingResult);

        // ACT
        ResponseEntity<ApiResponse<Void>> response = handler.handleValidationException(exception);

        // ASSERT
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().getMessage()).contains("someField");
    }

    @Test
    @DisplayName("Should handle MethodArgumentNotValidException with no errors (fallback)")
    void shouldHandleMethodArgumentNotValidExceptionWithNoErrors() {
        // ARRANGE
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.getFieldErrors()).thenReturn(new ArrayList<>());

        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        when(exception.getBindingResult()).thenReturn(bindingResult);

        // ACT
        ResponseEntity<ApiResponse<Void>> response = handler.handleValidationException(exception);

        // ASSERT
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().getMessage()).isEqualTo("Erro de validação");
    }

    @Test
    @DisplayName("Should handle DataIntegrityViolationException with Matrícula constraint")
    void shouldHandleDataIntegrityViolationExceptionWithMatricula() {
        // ARRANGE
        DataIntegrityViolationException exception =
            new DataIntegrityViolationException("Violação de constraint: matricula_unique");

        // ACT
        ResponseEntity<ApiResponse<Void>> response = handler.handleDataIntegrityViolation(exception);

        // ASSERT
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody().getMessage().toLowerCase()).contains("matrícula");
    }

    @Test
    @DisplayName("Should handle DataIntegrityViolationException with null message")
    void shouldHandleDataIntegrityViolationExceptionWithNullMessage() {
        // ARRANGE
        DataIntegrityViolationException exception =
            new DataIntegrityViolationException(null);

        // ACT
        ResponseEntity<ApiResponse<Void>> response = handler.handleDataIntegrityViolation(exception);

        // ASSERT
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody().getMessage().toLowerCase()).contains("dados duplicados");
    }

    @Test
    @DisplayName("Should handle general Exception")
    void shouldHandleGeneralException() {
        // ARRANGE
        Exception exception = new RuntimeException("Unexpected error");

        // ACT
        ResponseEntity<ApiResponse<Void>> response = handler.handleGeneralException(exception);

        // ASSERT
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().isSuccess()).isFalse();
        assertThat(response.getBody().getMessage().toLowerCase()).contains("erro");
    }

    @Test
    @DisplayName("Should handle Exception with different types")
    void shouldHandleExceptionWithDifferentTypes() {
        // ARRANGE
        Exception exception = new IllegalArgumentException("Invalid argument");

        // ACT
        ResponseEntity<ApiResponse<Void>> response = handler.handleGeneralException(exception);

        // ASSERT
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody().isSuccess()).isFalse();
    }
}

