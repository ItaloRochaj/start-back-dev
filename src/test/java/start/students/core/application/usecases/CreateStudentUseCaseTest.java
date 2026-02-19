package start.students.core.application.usecases;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import start.students.core.application.dtos.CreateStudentInputDTO;
import start.students.core.application.dtos.StudentOutputDTO;
import start.students.core.application.mappers.StudentMapper;
import start.students.core.domain.entities.Student;
import start.students.core.domain.exceptions.DomainException;
import start.students.core.ports.StudentRepositoryPort;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CreateStudentUseCase Tests")
class CreateStudentUseCaseTest {

    @Mock
    private StudentRepositoryPort studentRepository;

    @Mock
    private StudentMapper studentMapper;

    @InjectMocks
    private CreateStudentUseCase useCase;

    @Test
    @DisplayName("Should create student successfully when all data is valid")
    void shouldCreateStudentSuccessfully() {
        // ARRANGE
        CreateStudentInputDTO input = new CreateStudentInputDTO();
        input.setName("João Silva Santos");
        input.setCpf("12345678901");
        input.setEmail("joao@example.com");
        input.setPhone("11999999999");
        input.setPhoto(null);

        Student studentEntity = new Student();
        studentEntity.setId("uuid-1");
        studentEntity.setName("João Silva Santos");
        studentEntity.setCpf("12345678901");

        StudentOutputDTO expected = StudentOutputDTO.builder()
                .id("uuid-1")
                .name("João Silva Santos")
                .cpf("12345678901")
                .email("joao@example.com")
                .build();

        when(studentRepository.existsByCpf("12345678901")).thenReturn(false);
        when(studentRepository.existsByEmailIgnoreCase("joao@example.com")).thenReturn(false);
        when(studentMapper.toEntity(input)).thenReturn(studentEntity);
        when(studentRepository.save(studentEntity)).thenReturn(studentEntity);
        when(studentMapper.toOutputDTO(studentEntity)).thenReturn(expected);

        // ACT
        StudentOutputDTO result = useCase.execute(input);

        // ASSERT
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("uuid-1");
        assertThat(result.getName()).isEqualTo("João Silva Santos");
        verify(studentRepository, times(1)).existsByCpf("12345678901");
        verify(studentRepository, times(1)).existsByEmailIgnoreCase("joao@example.com");
        verify(studentRepository, times(1)).save(studentEntity);
    }

    @Test
    @DisplayName("Should throw exception when CPF already exists")
    void shouldThrowExceptionWhenCpfExists() {
        // ARRANGE
        CreateStudentInputDTO input = new CreateStudentInputDTO();
        input.setCpf("12345678901");
        input.setEmail("joao@example.com");

        when(studentRepository.existsByCpf("12345678901")).thenReturn(true);

        // ACT & ASSERT
        assertThatThrownBy(() -> useCase.execute(input))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("CPF já está cadastrado");

        verify(studentRepository, times(1)).existsByCpf("12345678901");
        verify(studentRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when email already exists")
    void shouldThrowExceptionWhenEmailExists() {
        // ARRANGE
        CreateStudentInputDTO input = new CreateStudentInputDTO();
        input.setCpf("12345678901");
        input.setEmail("joao@example.com");

        when(studentRepository.existsByCpf("12345678901")).thenReturn(false);
        when(studentRepository.existsByEmailIgnoreCase("joao@example.com")).thenReturn(true);

        // ACT & ASSERT
        assertThatThrownBy(() -> useCase.execute(input))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Email já está cadastrado");

        verify(studentRepository, times(1)).existsByCpf("12345678901");
        verify(studentRepository, times(1)).existsByEmailIgnoreCase("joao@example.com");
        verify(studentRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should handle case-insensitive email validation")
    void shouldHandleCaseInsensitiveEmailValidation() {
        // ARRANGE
        CreateStudentInputDTO input = new CreateStudentInputDTO();
        input.setCpf("12345678901");
        input.setEmail("Joao@EXAMPLE.COM");

        when(studentRepository.existsByCpf("12345678901")).thenReturn(false);
        when(studentRepository.existsByEmailIgnoreCase("Joao@EXAMPLE.COM")).thenReturn(true);

        // ACT & ASSERT
        assertThatThrownBy(() -> useCase.execute(input))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Email já está cadastrado");

        verify(studentRepository).existsByEmailIgnoreCase("Joao@EXAMPLE.COM");
    }
}

