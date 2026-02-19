package start.students.core.application.mappers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import start.students.adapters.outbound.repositories.StudentJpaRepository;
import start.students.core.application.dtos.CreateStudentInputDTO;
import start.students.core.application.dtos.StudentOutputDTO;
import start.students.core.application.dtos.UpdateStudentInputDTO;
import start.students.core.domain.entities.Student;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("StudentMapper Tests")
class StudentMapperTest {

    @Mock
    private StudentJpaRepository studentJpaRepository;

    @InjectMocks
    private StudentMapper mapper;

    @Test
    @DisplayName("Should convert CreateStudentInputDTO to Student entity")
    void shouldConvertCreateStudentInputDTOToEntity() {
        // ARRANGE
        CreateStudentInputDTO input = new CreateStudentInputDTO();
        input.setName("João Silva Santos");
        input.setCpf("12345678901");
        input.setEmail("joao@example.com");
        input.setPhone("11999999999");
        input.setPhoto(null);

        when(studentJpaRepository.findMaxMatricula()).thenReturn(null);

        // ACT
        Student result = mapper.toEntity(input);

        // ASSERT
        assertThat(result).isNotNull();
        assertThat(result.getId()).isNotNull();
        assertThat(result.getName()).isEqualTo("João Silva Santos");
        assertThat(result.getCpf()).isEqualTo("12345678901");
        assertThat(result.getEmail()).isEqualTo("joao@example.com");
        assertThat(result.getPhone()).isEqualTo("11999999999");
        assertThat(result.getStatus()).isEqualTo("Ativo");
        assertThat(result.getCreatedAt()).isNotNull();
        assertThat(result.getUpdatedAt()).isNotNull();
        assertThat(result.getMatricula()).isNotNull();
    }

    @Test
    @DisplayName("Should convert Student entity to StudentOutputDTO")
    void shouldConvertStudentEntityToOutputDTO() {
        // ARRANGE
        Student student = new Student();
        student.setId("uuid-1");
        student.setMatricula("2026000001");
        student.setName("João Silva");
        student.setCpf("12345678901");
        student.setEmail("joao@example.com");
        student.setPhone("11999999999");
        student.setPhoto(null);
        student.setStatus("Ativo");
        LocalDateTime now = LocalDateTime.now();
        student.setCreatedAt(now);
        student.setUpdatedAt(now);

        // ACT
        StudentOutputDTO result = mapper.toOutputDTO(student);

        // ASSERT
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("uuid-1");
        assertThat(result.getMatricula()).isEqualTo("2026000001");
        assertThat(result.getName()).isEqualTo("João Silva");
        assertThat(result.getCpf()).isEqualTo("12345678901");
        assertThat(result.getEmail()).isEqualTo("joao@example.com");
        assertThat(result.getPhone()).isEqualTo("11999999999");
        assertThat(result.getStatus()).isEqualTo("Ativo");
    }

    @Test
    @DisplayName("Should update entity from UpdateStudentInputDTO")
    void shouldUpdateEntityFromDTO() {
        // ARRANGE
        Student student = new Student();
        student.setId("uuid-1");
        student.setName("João Silva");
        student.setCpf("12345678901");
        student.setEmail("joao@example.com");
        student.setPhone("11999999999");
        student.setStatus("Ativo");

        UpdateStudentInputDTO input = new UpdateStudentInputDTO();
        input.setName("João Silva Santos");
        input.setPhone("11987654321");

        LocalDateTime originalCreatedAt = student.getCreatedAt();

        // ACT
        mapper.updateEntityFromDTO(input, student);

        // ASSERT
        assertThat(student.getName()).isEqualTo("João Silva Santos");
        assertThat(student.getPhone()).isEqualTo("11987654321");
        assertThat(student.getCpf()).isEqualTo("12345678901");
        assertThat(student.getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("Should not update fields with null values")
    void shouldNotUpdateFieldsWithNullValues() {
        // ARRANGE
        Student student = new Student();
        student.setId("uuid-1");
        student.setName("João Silva");
        student.setCpf("12345678901");

        UpdateStudentInputDTO input = new UpdateStudentInputDTO();
        input.setName(null);
        input.setCpf(null);

        // ACT
        mapper.updateEntityFromDTO(input, student);

        // ASSERT
        assertThat(student.getName()).isEqualTo("João Silva");
        assertThat(student.getCpf()).isEqualTo("12345678901");
    }

    @Test
    @DisplayName("Should not update empty string fields")
    void shouldNotUpdateEmptyStringFields() {
        // ARRANGE
        Student student = new Student();
        student.setId("uuid-1");
        student.setName("João Silva");
        student.setPhone("11999999999");

        UpdateStudentInputDTO input = new UpdateStudentInputDTO();
        input.setName("   ");
        input.setPhone("");

        // ACT
        mapper.updateEntityFromDTO(input, student);

        // ASSERT
        assertThat(student.getName()).isEqualTo("João Silva");
        assertThat(student.getPhone()).isEqualTo("11999999999");
    }

    @Test
    @DisplayName("Should update all provided fields")
    void shouldUpdateAllProvidedFields() {
        // ARRANGE
        Student student = new Student();
        student.setName("João Silva");
        student.setCpf("12345678901");
        student.setEmail("joao@example.com");
        student.setPhone("11999999999");
        student.setPhoto(null);
        student.setStatus("Ativo");

        UpdateStudentInputDTO input = new UpdateStudentInputDTO();
        input.setName("Maria Santos");
        input.setCpf("98765432100");
        input.setEmail("maria@example.com");
        input.setPhone("11988888888");
        input.setPhoto("photo-base64");
        input.setStatus("Inativo");

        // ACT
        mapper.updateEntityFromDTO(input, student);

        // ASSERT
        assertThat(student.getName()).isEqualTo("Maria Santos");
        assertThat(student.getCpf()).isEqualTo("98765432100");
        assertThat(student.getEmail()).isEqualTo("maria@example.com");
        assertThat(student.getPhone()).isEqualTo("11988888888");
        assertThat(student.getPhoto()).isEqualTo("photo-base64");
        assertThat(student.getStatus()).isEqualTo("Inativo");
    }
}

