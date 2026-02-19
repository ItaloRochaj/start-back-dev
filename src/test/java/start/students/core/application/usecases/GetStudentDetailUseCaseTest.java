package start.students.core.application.usecases;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import start.students.core.application.dtos.StudentOutputDTO;
import start.students.core.application.mappers.StudentMapper;
import start.students.core.domain.entities.Student;
import start.students.core.domain.exceptions.StudentNotFoundException;
import start.students.core.ports.StudentRepositoryPort;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("GetStudentDetailUseCase Tests")
class GetStudentDetailUseCaseTest {

    @Mock
    private StudentRepositoryPort studentRepository;

    @Mock
    private StudentMapper studentMapper;

    @InjectMocks
    private GetStudentDetailUseCase useCase;

    @Test
    @DisplayName("Should get student details successfully")
    void shouldGetStudentDetailsSuccessfully() {
        // ARRANGE
        String studentId = "uuid-1";

        Student student = new Student();
        student.setId("uuid-1");
        student.setName("João Silva");
        student.setCpf("12345678901");
        student.setEmail("joao@example.com");

        StudentOutputDTO expected = StudentOutputDTO.builder()
                .id("uuid-1")
                .name("João Silva")
                .cpf("12345678901")
                .email("joao@example.com")
                .build();

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        when(studentMapper.toOutputDTO(student)).thenReturn(expected);

        // ACT
        StudentOutputDTO result = useCase.execute(studentId);

        // ASSERT
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("uuid-1");
        assertThat(result.getName()).isEqualTo("João Silva");
        verify(studentRepository, times(1)).findById(studentId);
        verify(studentMapper, times(1)).toOutputDTO(student);
    }

    @Test
    @DisplayName("Should throw exception when student not found")
    void shouldThrowExceptionWhenStudentNotFound() {
        // ARRANGE
        String studentId = "nonexistent-uuid";

        when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

        // ACT & ASSERT
        assertThatThrownBy(() -> useCase.execute(studentId))
                .isInstanceOf(StudentNotFoundException.class)
                .hasMessageContaining("Estudante não encontrado");

        verify(studentRepository, times(1)).findById(studentId);
        verify(studentMapper, never()).toOutputDTO(any());
    }

    @Test
    @DisplayName("Should verify mapper is called with correct student object")
    void shouldVerifyMapperIsCalledWithCorrectStudentObject() {
        // ARRANGE
        String studentId = "uuid-1";

        Student student = new Student();
        student.setId("uuid-1");
        student.setName("Maria Santos");

        StudentOutputDTO expected = StudentOutputDTO.builder()
                .id("uuid-1")
                .name("Maria Santos")
                .build();

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        when(studentMapper.toOutputDTO(student)).thenReturn(expected);

        // ACT
        useCase.execute(studentId);

        // ASSERT
        verify(studentMapper).toOutputDTO(student);
    }
}

