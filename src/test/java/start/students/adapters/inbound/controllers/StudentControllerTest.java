package start.students.adapters.inbound.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import start.students.adapters.inbound.http.ApiResponse;
import start.students.core.application.dtos.CreateStudentInputDTO;
import start.students.core.application.dtos.StudentOutputDTO;
import start.students.core.application.dtos.StudentPagedOutputDTO;
import start.students.core.application.dtos.UpdateStudentInputDTO;
import start.students.core.application.usecases.*;
import start.students.core.domain.exceptions.DomainException;
import start.students.core.domain.exceptions.StudentNotFoundException;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("StudentController Tests")
class StudentControllerTest {

    @Mock
    private CreateStudentUseCase createStudentUseCase;

    @Mock
    private GetStudentDetailUseCase getStudentDetailUseCase;

    @Mock
    private UpdateStudentUseCase updateStudentUseCase;

    @Mock
    private DeleteStudentUseCase deleteStudentUseCase;

    @Mock
    private ListStudentsUseCase listStudentsUseCase;

    @InjectMocks
    private StudentController controller;

    @Test
    @DisplayName("Should create student successfully")
    void shouldCreateStudentSuccessfully() {
        // ARRANGE
        CreateStudentInputDTO input = new CreateStudentInputDTO();
        input.setName("João Silva");
        input.setCpf("12345678901");
        input.setEmail("joao@example.com");
        input.setPhone("11999999999");

        StudentOutputDTO response = StudentOutputDTO.builder()
                .id("uuid-1")
                .name("João Silva")
                .cpf("12345678901")
                .email("joao@example.com")
                .status("Ativo")
                .build();

        when(createStudentUseCase.execute(any(CreateStudentInputDTO.class))).thenReturn(response);

        // ACT
        ResponseEntity<ApiResponse<StudentOutputDTO>> result = controller.create(input);

        // ASSERT
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody().isSuccess()).isTrue();
        assertThat(result.getBody().getData().getId()).isEqualTo("uuid-1");
        verify(createStudentUseCase, times(1)).execute(any(CreateStudentInputDTO.class));
    }

    @Test
    @DisplayName("Should return error when creating student with duplicate CPF")
    void shouldReturnErrorWhenCreatingStudentWithDuplicateCPF() {
        // ARRANGE
        CreateStudentInputDTO input = new CreateStudentInputDTO();
        when(createStudentUseCase.execute(any(CreateStudentInputDTO.class)))
                .thenThrow(new DomainException("CPF já está cadastrado"));

        // ACT
        ResponseEntity<ApiResponse<StudentOutputDTO>> result = controller.create(input);

        // ASSERT
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(result.getBody().isSuccess()).isFalse();
        verify(createStudentUseCase, times(1)).execute(any(CreateStudentInputDTO.class));
    }

    @Test
    @DisplayName("Should get student details successfully")
    void shouldGetStudentDetailsSuccessfully() {
        // ARRANGE
        StudentOutputDTO response = StudentOutputDTO.builder()
                .id("uuid-1")
                .name("João Silva")
                .build();

        when(getStudentDetailUseCase.execute("uuid-1")).thenReturn(response);

        // ACT
        ResponseEntity<ApiResponse<StudentOutputDTO>> result = controller.getDetail("uuid-1");

        // ASSERT
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody().isSuccess()).isTrue();
        verify(getStudentDetailUseCase, times(1)).execute("uuid-1");
    }

    @Test
    @DisplayName("Should return error when student not found")
    void shouldReturnErrorWhenStudentNotFound() {
        // ARRANGE
        when(getStudentDetailUseCase.execute("nonexistent"))
                .thenThrow(new StudentNotFoundException("Estudante não encontrado"));

        // ACT
        ResponseEntity<ApiResponse<StudentOutputDTO>> result = controller.getDetail("nonexistent");

        // ASSERT
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(result.getBody().isSuccess()).isFalse();
    }

    @Test
    @DisplayName("Should update student successfully")
    void shouldUpdateStudentSuccessfully() {
        // ARRANGE
        UpdateStudentInputDTO input = new UpdateStudentInputDTO();
        input.setName("João Silva Santos");

        StudentOutputDTO response = StudentOutputDTO.builder()
                .id("uuid-1")
                .name("João Silva Santos")
                .build();

        when(updateStudentUseCase.execute(eq("uuid-1"), any(UpdateStudentInputDTO.class)))
                .thenReturn(response);

        // ACT
        ResponseEntity<ApiResponse<StudentOutputDTO>> result = controller.update("uuid-1", input);

        // ASSERT
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody().isSuccess()).isTrue();
        verify(updateStudentUseCase, times(1)).execute(eq("uuid-1"), any(UpdateStudentInputDTO.class));
    }

    @Test
    @DisplayName("Should delete student successfully")
    void shouldDeleteStudentSuccessfully() {
        // ARRANGE
        doNothing().when(deleteStudentUseCase).execute("uuid-1");

        // ACT
        ResponseEntity<ApiResponse<Void>> result = controller.delete("uuid-1");

        // ASSERT
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody().isSuccess()).isTrue();
        verify(deleteStudentUseCase, times(1)).execute("uuid-1");
    }

    @Test
    @DisplayName("Should list students successfully")
    void shouldListStudentsSuccessfully() {
        // ARRANGE
        StudentOutputDTO student1 = StudentOutputDTO.builder().id("uuid-1").name("João Silva").build();
        StudentOutputDTO student2 = StudentOutputDTO.builder().id("uuid-2").name("Maria Santos").build();

        StudentPagedOutputDTO response = StudentPagedOutputDTO.builder()
                .content(Arrays.asList(student1, student2))
                .page(0)
                .size(10)
                .totalElements(2)
                .totalPages(1)
                .first(true)
                .last(true)
                .build();

        when(listStudentsUseCase.execute(0, 10, null, "name")).thenReturn(response);

        // ACT
        ResponseEntity<ApiResponse<StudentPagedOutputDTO>> result = controller.list(0, 10, null, "name");

        // ASSERT
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody().isSuccess()).isTrue();
        assertThat(result.getBody().getData().getContent()).hasSize(2);
        verify(listStudentsUseCase, times(1)).execute(0, 10, null, "name");
    }

    @Test
    @DisplayName("Should validate CPF exists")
    void shouldValidateCpfExists() {
        // ARRANGE
        when(listStudentsUseCase.validateCpfExists("12345678901")).thenReturn(true);

        // ACT
        ResponseEntity<ApiResponse<Boolean>> result = controller.validateCpf("12345678901");

        // ASSERT
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody().isSuccess()).isTrue();
        assertThat(result.getBody().getData()).isTrue();
        verify(listStudentsUseCase, times(1)).validateCpfExists("12345678901");
    }

    @Test
    @DisplayName("Should validate email exists")
    void shouldValidateEmailExists() {
        // ARRANGE
        when(listStudentsUseCase.validateEmailExists("joao@example.com")).thenReturn(false);

        // ACT
        ResponseEntity<ApiResponse<Boolean>> result = controller.validateEmail("joao@example.com");

        // ASSERT
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody().isSuccess()).isTrue();
        assertThat(result.getBody().getData()).isFalse();
        verify(listStudentsUseCase, times(1)).validateEmailExists("joao@example.com");
    }

    @Test
    @DisplayName("Should search students by name")
    void shouldSearchStudentsByName() {
        // ARRANGE
        StudentOutputDTO student = StudentOutputDTO.builder().id("uuid-1").name("João Silva").build();

        StudentPagedOutputDTO response = StudentPagedOutputDTO.builder()
                .content(Arrays.asList(student))
                .page(0)
                .size(10)
                .totalElements(1)
                .totalPages(1)
                .first(true)
                .last(true)
                .build();

        when(listStudentsUseCase.execute(0, 10, "João", "name")).thenReturn(response);

        // ACT
        ResponseEntity<ApiResponse<StudentPagedOutputDTO>> result = controller.list(0, 10, "João", "name");

        // ASSERT
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody().getData().getContent()).hasSize(1);
        verify(listStudentsUseCase, times(1)).execute(0, 10, "João", "name");
    }
}

