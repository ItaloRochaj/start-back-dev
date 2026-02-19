package start.students.core.application.usecases;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import start.students.core.application.dtos.StudentOutputDTO;
import start.students.core.application.dtos.StudentPagedOutputDTO;
import start.students.core.application.mappers.StudentMapper;
import start.students.core.domain.entities.Student;
import start.students.core.ports.StudentRepositoryPort;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ListStudentsUseCase Tests")
class ListStudentsUseCaseTest {

    @Mock
    private StudentRepositoryPort studentRepository;

    @Mock
    private StudentMapper studentMapper;

    @InjectMocks
    private ListStudentsUseCase useCase;

    @Test
    @DisplayName("Should list all students successfully")
    void shouldListAllStudentsSuccessfully() {
        // ARRANGE
        Student student1 = new Student();
        student1.setId("uuid-1");
        student1.setName("João Silva");

        Student student2 = new Student();
        student2.setId("uuid-2");
        student2.setName("Maria Santos");

        List<Student> studentList = Arrays.asList(student1, student2);
        Page<Student> studentPage = new PageImpl<>(studentList, PageRequest.of(0, 10), 2);

        StudentOutputDTO dto1 = StudentOutputDTO.builder().id("uuid-1").name("João Silva").build();
        StudentOutputDTO dto2 = StudentOutputDTO.builder().id("uuid-2").name("Maria Santos").build();

        when(studentRepository.findAll(any())).thenReturn(studentPage);
        when(studentMapper.toOutputDTO(student1)).thenReturn(dto1);
        when(studentMapper.toOutputDTO(student2)).thenReturn(dto2);

        // ACT
        StudentPagedOutputDTO result = useCase.execute(0, 10, null, "name");

        // ASSERT
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getPage()).isEqualTo(0);
        assertThat(result.getSize()).isEqualTo(10);
        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.isFirst()).isTrue();
        assertThat(result.isLast()).isTrue();

        verify(studentRepository, times(1)).findAll(any());
    }

    @Test
    @DisplayName("Should search students by name")
    void shouldSearchStudentsByName() {
        // ARRANGE
        Student student = new Student();
        student.setId("uuid-1");
        student.setName("João Silva");

        List<Student> studentList = Arrays.asList(student);
        Page<Student> studentPage = new PageImpl<>(studentList, PageRequest.of(0, 10), 1);

        StudentOutputDTO dto = StudentOutputDTO.builder().id("uuid-1").name("João Silva").build();

        when(studentRepository.findByNameContainingIgnoreCase(eq("João"), any())).thenReturn(studentPage);
        when(studentMapper.toOutputDTO(student)).thenReturn(dto);

        // ACT
        StudentPagedOutputDTO result = useCase.execute(0, 10, "João", "name");

        // ASSERT
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        verify(studentRepository).findByNameContainingIgnoreCase(eq("João"), any());
    }

    @Test
    @DisplayName("Should search students by CPF")
    void shouldSearchStudentsByCpf() {
        // ARRANGE
        Student student = new Student();
        student.setId("uuid-1");
        student.setCpf("12345678901");

        List<Student> studentList = Arrays.asList(student);
        Page<Student> studentPage = new PageImpl<>(studentList, PageRequest.of(0, 10), 1);

        StudentOutputDTO dto = StudentOutputDTO.builder().id("uuid-1").cpf("12345678901").build();

        when(studentRepository.findByCpfContaining(eq("12345678901"), any())).thenReturn(studentPage);
        when(studentMapper.toOutputDTO(student)).thenReturn(dto);

        // ACT
        StudentPagedOutputDTO result = useCase.execute(0, 10, "12345678901", "cpf");

        // ASSERT
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        verify(studentRepository).findByCpfContaining(eq("12345678901"), any());
    }

    @Test
    @DisplayName("Should search students by email")
    void shouldSearchStudentsByEmail() {
        // ARRANGE
        Student student = new Student();
        student.setId("uuid-1");
        student.setEmail("joao@example.com");

        List<Student> studentList = Arrays.asList(student);
        Page<Student> studentPage = new PageImpl<>(studentList, PageRequest.of(0, 10), 1);

        StudentOutputDTO dto = StudentOutputDTO.builder().id("uuid-1").email("joao@example.com").build();

        when(studentRepository.findByEmailContainingIgnoreCase(eq("joao"), any())).thenReturn(studentPage);
        when(studentMapper.toOutputDTO(student)).thenReturn(dto);

        // ACT
        StudentPagedOutputDTO result = useCase.execute(0, 10, "joao", "email");

        // ASSERT
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        verify(studentRepository).findByEmailContainingIgnoreCase(eq("joao"), any());
    }

    @Test
    @DisplayName("Should search students by matricula")
    void shouldSearchStudentsByMatricula() {
        // ARRANGE
        Student student = new Student();
        student.setId("uuid-1");
        student.setMatricula("2026000001");

        List<Student> studentList = Arrays.asList(student);
        Page<Student> studentPage = new PageImpl<>(studentList, PageRequest.of(0, 10), 1);

        StudentOutputDTO dto = StudentOutputDTO.builder().id("uuid-1").matricula("2026000001").build();

        when(studentRepository.findByMatriculaContaining(eq("2026"), any())).thenReturn(studentPage);
        when(studentMapper.toOutputDTO(student)).thenReturn(dto);

        // ACT
        StudentPagedOutputDTO result = useCase.execute(0, 10, "2026", "matricula");

        // ASSERT
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        verify(studentRepository).findByMatriculaContaining(eq("2026"), any());
    }

    @Test
    @DisplayName("Should validate if CPF exists")
    void shouldValidateCpfExists() {
        // ARRANGE
        when(studentRepository.existsByCpf("12345678901")).thenReturn(true);

        // ACT
        boolean result = useCase.validateCpfExists("12345678901");

        // ASSERT
        assertThat(result).isTrue();
        verify(studentRepository).existsByCpf("12345678901");
    }

    @Test
    @DisplayName("Should validate if email exists")
    void shouldValidateEmailExists() {
        // ARRANGE
        when(studentRepository.existsByEmailIgnoreCase("joao@example.com")).thenReturn(true);

        // ACT
        boolean result = useCase.validateEmailExists("joao@example.com");

        // ASSERT
        assertThat(result).isTrue();
        verify(studentRepository).existsByEmailIgnoreCase("joao@example.com");
    }

    @Test
    @DisplayName("Should handle empty search with null search parameter")
    void shouldHandleEmptySearchWithNullSearchParameter() {
        // ARRANGE
        Student student = new Student();
        student.setId("uuid-1");
        student.setName("João Silva");

        List<Student> studentList = Arrays.asList(student);
        Page<Student> studentPage = new PageImpl<>(studentList, PageRequest.of(0, 10), 1);

        StudentOutputDTO dto = StudentOutputDTO.builder().id("uuid-1").name("João Silva").build();

        when(studentRepository.findAll(any())).thenReturn(studentPage);
        when(studentMapper.toOutputDTO(student)).thenReturn(dto);

        // ACT
        StudentPagedOutputDTO result = useCase.execute(0, 10, null, "name");

        // ASSERT
        assertThat(result).isNotNull();
        verify(studentRepository).findAll(any());
    }

    @Test
    @DisplayName("Should handle empty search with empty search string")
    void shouldHandleEmptySearchWithEmptyString() {
        // ARRANGE
        Student student = new Student();
        student.setId("uuid-1");
        student.setName("João Silva");

        List<Student> studentList = Arrays.asList(student);
        Page<Student> studentPage = new PageImpl<>(studentList, PageRequest.of(0, 10), 1);

        StudentOutputDTO dto = StudentOutputDTO.builder().id("uuid-1").name("João Silva").build();

        when(studentRepository.findAll(any())).thenReturn(studentPage);
        when(studentMapper.toOutputDTO(student)).thenReturn(dto);

        // ACT
        StudentPagedOutputDTO result = useCase.execute(0, 10, "   ", "name");

        // ASSERT
        assertThat(result).isNotNull();
        verify(studentRepository).findAll(any());
    }
}

