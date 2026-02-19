package start.students.core.application.usecases;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import start.students.core.application.dtos.StudentOutputDTO;
import start.students.core.application.dtos.UpdateStudentInputDTO;
import start.students.core.application.mappers.StudentMapper;
import start.students.core.domain.entities.Student;
import start.students.core.domain.exceptions.DomainException;
import start.students.core.domain.exceptions.StudentNotFoundException;
import start.students.core.ports.StudentRepositoryPort;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UpdateStudentUseCase Tests")
class UpdateStudentUseCaseTest {

    @Mock
    private StudentRepositoryPort studentRepository;

    @Mock
    private StudentMapper studentMapper;

    @InjectMocks
    private UpdateStudentUseCase useCase;

    @Test
    @DisplayName("Should update student successfully")
    void shouldUpdateStudentSuccessfully() {
        // ARRANGE
        String studentId = "uuid-1";

        UpdateStudentInputDTO input = new UpdateStudentInputDTO();
        input.setName("João Silva Santos");
        input.setPhone("11987654321");

        Student existingStudent = new Student();
        existingStudent.setId("uuid-1");
        existingStudent.setName("João Silva");
        existingStudent.setCpf("12345678901");
        existingStudent.setEmail("joao@example.com");

        Student updatedStudent = new Student();
        updatedStudent.setId("uuid-1");
        updatedStudent.setName("João Silva Santos");
        updatedStudent.setCpf("12345678901");
        updatedStudent.setEmail("joao@example.com");
        updatedStudent.setPhone("11987654321");

        StudentOutputDTO expected = StudentOutputDTO.builder()
                .id("uuid-1")
                .name("João Silva Santos")
                .cpf("12345678901")
                .email("joao@example.com")
                .phone("11987654321")
                .build();

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(existingStudent));
        doNothing().when(studentMapper).updateEntityFromDTO(input, existingStudent);
        when(studentRepository.save(existingStudent)).thenReturn(updatedStudent);
        when(studentMapper.toOutputDTO(updatedStudent)).thenReturn(expected);

        // ACT
        StudentOutputDTO result = useCase.execute(studentId, input);

        // ASSERT
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("João Silva Santos");
        verify(studentRepository, times(1)).findById(studentId);
        verify(studentMapper, times(1)).updateEntityFromDTO(input, existingStudent);
        verify(studentRepository, times(1)).save(existingStudent);
    }

    @Test
    @DisplayName("Should throw exception when student not found")
    void shouldThrowExceptionWhenStudentNotFound() {
        // ARRANGE
        String studentId = "nonexistent-uuid";
        UpdateStudentInputDTO input = new UpdateStudentInputDTO();

        when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

        // ACT & ASSERT
        assertThatThrownBy(() -> useCase.execute(studentId, input))
                .isInstanceOf(StudentNotFoundException.class)
                .hasMessageContaining("Estudante não encontrado");

        verify(studentRepository, times(1)).findById(studentId);
        verify(studentRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when CPF already exists")
    void shouldThrowExceptionWhenCpfAlreadyExists() {
        // ARRANGE
        String studentId = "uuid-1";

        UpdateStudentInputDTO input = new UpdateStudentInputDTO();
        input.setCpf("99999999999");

        Student existingStudent = new Student();
        existingStudent.setId("uuid-1");
        existingStudent.setCpf("12345678901");

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(existingStudent));
        when(studentRepository.existsByCpf("99999999999")).thenReturn(true);

        // ACT & ASSERT
        assertThatThrownBy(() -> useCase.execute(studentId, input))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("CPF já está cadastrado");

        verify(studentRepository, times(1)).findById(studentId);
        verify(studentRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when email already exists")
    void shouldThrowExceptionWhenEmailAlreadyExists() {
        // ARRANGE
        String studentId = "uuid-1";

        UpdateStudentInputDTO input = new UpdateStudentInputDTO();
        input.setEmail("newemail@example.com");

        Student existingStudent = new Student();
        existingStudent.setId("uuid-1");
        existingStudent.setEmail("joao@example.com");

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(existingStudent));
        when(studentRepository.existsByEmailIgnoreCase("newemail@example.com")).thenReturn(true);

        // ACT & ASSERT
        assertThatThrownBy(() -> useCase.execute(studentId, input))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Email já está cadastrado");

        verify(studentRepository, times(1)).findById(studentId);
        verify(studentRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should allow updating CPF with same CPF")
    void shouldAllowUpdatingWithSameCpf() {
        // ARRANGE
        String studentId = "uuid-1";

        UpdateStudentInputDTO input = new UpdateStudentInputDTO();
        input.setCpf("12345678901");
        input.setName("João Silva Santos");

        Student existingStudent = new Student();
        existingStudent.setId("uuid-1");
        existingStudent.setCpf("12345678901");

        Student updatedStudent = new Student();
        updatedStudent.setId("uuid-1");
        updatedStudent.setCpf("12345678901");
        updatedStudent.setName("João Silva Santos");

        StudentOutputDTO expected = StudentOutputDTO.builder()
                .id("uuid-1")
                .cpf("12345678901")
                .name("João Silva Santos")
                .build();

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(existingStudent));
        doNothing().when(studentMapper).updateEntityFromDTO(input, existingStudent);
        when(studentRepository.save(existingStudent)).thenReturn(updatedStudent);
        when(studentMapper.toOutputDTO(updatedStudent)).thenReturn(expected);

        // ACT
        StudentOutputDTO result = useCase.execute(studentId, input);

        // ASSERT
        assertThat(result).isNotNull();
        verify(studentRepository, never()).existsByCpf("12345678901");
        verify(studentRepository, times(1)).save(existingStudent);
    }

    @Test
    @DisplayName("Should allow updating email with same email (case-insensitive)")
    void shouldAllowUpdatingWithSameEmailCaseInsensitive() {
        // ARRANGE
        String studentId = "uuid-1";

        UpdateStudentInputDTO input = new UpdateStudentInputDTO();
        input.setEmail("joao@EXAMPLE.COM");
        input.setName("João Silva Santos");

        Student existingStudent = new Student();
        existingStudent.setId("uuid-1");
        existingStudent.setEmail("joao@example.com");

        Student updatedStudent = new Student();
        updatedStudent.setId("uuid-1");
        updatedStudent.setEmail("joao@EXAMPLE.COM");
        updatedStudent.setName("João Silva Santos");

        StudentOutputDTO expected = StudentOutputDTO.builder()
                .id("uuid-1")
                .email("joao@EXAMPLE.COM")
                .name("João Silva Santos")
                .build();

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(existingStudent));
        doNothing().when(studentMapper).updateEntityFromDTO(input, existingStudent);
        when(studentRepository.save(existingStudent)).thenReturn(updatedStudent);
        when(studentMapper.toOutputDTO(updatedStudent)).thenReturn(expected);

        // ACT
        StudentOutputDTO result = useCase.execute(studentId, input);

        // ASSERT
        assertThat(result).isNotNull();
        verify(studentRepository, never()).existsByEmailIgnoreCase("joao@EXAMPLE.COM");
        verify(studentRepository, times(1)).save(existingStudent);
    }
}

