package start.students.adapters.outbound.persistence.adapters;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import start.students.adapters.outbound.persistence.entities.StudentJpaEntity;
import start.students.adapters.outbound.repositories.StudentJpaRepository;
import start.students.core.domain.entities.Student;
import start.students.core.domain.valueobjects.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@DisplayName("StudentPersistenceAdapter Tests")
class StudentPersistenceAdapterTest {

    @Mock
    private StudentJpaRepository repository;

    @InjectMocks
    private StudentPersistenceAdapter adapter;

    private StudentJpaEntity studentEntity;
    private Student studentDomain;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        pageable = PageRequest.of(0, 10);

        // Create test data
        String studentId = "550e8400-e29b-41d4-a716-446655440000";
        LocalDateTime now = LocalDateTime.now();

        studentEntity = new StudentJpaEntity(
                studentId,
                "MAT001",
                "Test Student",
                "11144477735",
                "test@gmail.com",
                "11999999999",
                null,
                "ACTIVE",
                now,
                now
        );

        studentDomain = new Student(
                studentId,
                "MAT001",
                "Test Student",
                "11144477735",
                "test@gmail.com",
                "11999999999",
                null,
                "ACTIVE",
                now,
                now
        );
    }

    @Test
    @DisplayName("should save student successfully")
    void shouldSaveStudentSuccessfully() {
        // Arrange
        when(repository.save(any(StudentJpaEntity.class))).thenReturn(studentEntity);

        // Act
        Student savedStudent = adapter.save(studentDomain);

        // Assert
        assertThat(savedStudent)
                .isNotNull()
                .hasFieldOrPropertyWithValue("matricula", "MAT001")
                .hasFieldOrPropertyWithValue("name", "Test Student");

        verify(repository, times(1)).save(any(StudentJpaEntity.class));
    }

    @Test
    @DisplayName("should find student by id successfully")
    void shouldFindStudentByIdSuccessfully() {
        // Arrange
        String studentId = "550e8400-e29b-41d4-a716-446655440000";
        when(repository.findById(studentId)).thenReturn(Optional.of(studentEntity));

        // Act
        Optional<Student> foundStudent = adapter.findById(studentId);

        // Assert
        assertThat(foundStudent)
                .isPresent()
                .hasValueSatisfying(student ->
                        assertThat(student.getMatricula()).isEqualTo("MAT001")
                );

        verify(repository, times(1)).findById(studentId);
    }

    @Test
    @DisplayName("should return empty when student not found by id")
    void shouldReturnEmptyWhenStudentNotFoundById() {
        // Arrange
        String nonExistentId = "non-existent-id";
        when(repository.findById(nonExistentId)).thenReturn(Optional.empty());

        // Act
        Optional<Student> foundStudent = adapter.findById(nonExistentId);

        // Assert
        assertThat(foundStudent).isEmpty();

        verify(repository, times(1)).findById(nonExistentId);
    }

    @Test
    @DisplayName("should delete student by id successfully")
    void shouldDeleteStudentByIdSuccessfully() {
        // Arrange
        String studentId = "550e8400-e29b-41d4-a716-446655440000";
        doNothing().when(repository).deleteById(studentId);

        // Act
        adapter.deleteById(studentId);

        // Assert
        verify(repository, times(1)).deleteById(studentId);
    }

    @Test
    @DisplayName("should find all students with pagination")
    void shouldFindAllStudentsWithPagination() {
        // Arrange
        Page<StudentJpaEntity> entityPage = new PageImpl<>(List.of(studentEntity), pageable, 1);
        when(repository.findAll(pageable)).thenReturn(entityPage);

        // Act
        Page<Student> resultPage = adapter.findAll(pageable);

        // Assert
        assertThat(resultPage)
                .isNotNull()
                .hasSize(1)
                .allSatisfy(student -> assertThat(student.getName()).isEqualTo("Test Student"));

        verify(repository, times(1)).findAll(pageable);
    }

    @Test
    @DisplayName("should find students by name containing ignore case")
    void shouldFindStudentsByNameContainingIgnoreCase() {
        // Arrange
        String searchName = "test";
        Page<StudentJpaEntity> entityPage = new PageImpl<>(List.of(studentEntity), pageable, 1);
        when(repository.findByNameContainingIgnoreCase(searchName, pageable)).thenReturn(entityPage);

        // Act
        Page<Student> resultPage = adapter.findByNameContainingIgnoreCase(searchName, pageable);

        // Assert
        assertThat(resultPage)
                .isNotNull()
                .hasSize(1);

        verify(repository, times(1)).findByNameContainingIgnoreCase(searchName, pageable);
    }

    @Test
    @DisplayName("should find students by CPF containing")
    void shouldFindStudentsByCpfContaining() {
        // Arrange
        String cpfSearchTerm = "111444";
        Page<StudentJpaEntity> entityPage = new PageImpl<>(List.of(studentEntity), pageable, 1);
        when(repository.findByCpfContaining(cpfSearchTerm, pageable)).thenReturn(entityPage);

        // Act
        Page<Student> resultPage = adapter.findByCpfContaining(cpfSearchTerm, pageable);

        // Assert
        assertThat(resultPage)
                .isNotNull()
                .hasSize(1);

        verify(repository, times(1)).findByCpfContaining(cpfSearchTerm, pageable);
    }

    @Test
    @DisplayName("should find students by email containing ignore case")
    void shouldFindStudentsByEmailContainingIgnoreCase() {
        // Arrange
        String emailSearchTerm = "test";
        Page<StudentJpaEntity> entityPage = new PageImpl<>(List.of(studentEntity), pageable, 1);
        when(repository.findByEmailContainingIgnoreCase(emailSearchTerm, pageable)).thenReturn(entityPage);

        // Act
        Page<Student> resultPage = adapter.findByEmailContainingIgnoreCase(emailSearchTerm, pageable);

        // Assert
        assertThat(resultPage)
                .isNotNull()
                .hasSize(1);

        verify(repository, times(1)).findByEmailContainingIgnoreCase(emailSearchTerm, pageable);
    }

    @Test
    @DisplayName("should find students by id containing")
    void shouldFindStudentsByIdContaining() {
        // Arrange
        String idSearchTerm = "550e";
        Page<StudentJpaEntity> entityPage = new PageImpl<>(List.of(studentEntity), pageable, 1);
        when(repository.findByIdContaining(idSearchTerm, pageable)).thenReturn(entityPage);

        // Act
        Page<Student> resultPage = adapter.findByIdContaining(idSearchTerm, pageable);

        // Assert
        assertThat(resultPage)
                .isNotNull()
                .hasSize(1);

        verify(repository, times(1)).findByIdContaining(idSearchTerm, pageable);
    }

    @Test
    @DisplayName("should find students by matricula containing")
    void shouldFindStudentsByMatriculaContaining() {
        // Arrange
        String matriculaSearchTerm = "MAT";
        Page<StudentJpaEntity> entityPage = new PageImpl<>(List.of(studentEntity), pageable, 1);
        when(repository.findByMatriculaContaining(matriculaSearchTerm, pageable)).thenReturn(entityPage);

        // Act
        Page<Student> resultPage = adapter.findByMatriculaContaining(matriculaSearchTerm, pageable);

        // Assert
        assertThat(resultPage)
                .isNotNull()
                .hasSize(1);

        verify(repository, times(1)).findByMatriculaContaining(matriculaSearchTerm, pageable);
    }

    @Test
    @DisplayName("should check if student exists by CPF")
    void shouldCheckIfStudentExistsByCpf() {
        // Arrange
        String cpf = "11144477735";
        when(repository.existsByCpf(cpf)).thenReturn(true);

        // Act
        boolean exists = adapter.existsByCpf(cpf);

        // Assert
        assertThat(exists).isTrue();

        verify(repository, times(1)).existsByCpf(cpf);
    }

    @Test
    @DisplayName("should return false when student does not exist by CPF")
    void shouldReturnFalseWhenStudentDoesNotExistByCpf() {
        // Arrange
        String cpf = "11111111111";
        when(repository.existsByCpf(cpf)).thenReturn(false);

        // Act
        boolean exists = adapter.existsByCpf(cpf);

        // Assert
        assertThat(exists).isFalse();

        verify(repository, times(1)).existsByCpf(cpf);
    }

    @Test
    @DisplayName("should check if student exists by email")
    void shouldCheckIfStudentExistsByEmail() {
        // Arrange
        String email = "test@gmail.com";
        when(repository.existsByEmail(email)).thenReturn(true);

        // Act
        boolean exists = adapter.existsByEmail(email);

        // Assert
        assertThat(exists).isTrue();

        verify(repository, times(1)).existsByEmail(email);
    }

    @Test
    @DisplayName("should check if student exists by email ignore case")
    void shouldCheckIfStudentExistsByEmailIgnoreCase() {
        // Arrange
        String email = "TEST@GMAIL.COM";
        when(repository.existsByEmailIgnoreCase(email)).thenReturn(true);

        // Act
        boolean exists = adapter.existsByEmailIgnoreCase(email);

        // Assert
        assertThat(exists).isTrue();

        verify(repository, times(1)).existsByEmailIgnoreCase(email);
    }

    @Test
    @DisplayName("should handle empty page result")
    void shouldHandleEmptyPageResult() {
        // Arrange
        Page<StudentJpaEntity> emptyPage = new PageImpl<>(List.of(), pageable, 0);
        when(repository.findAll(pageable)).thenReturn(emptyPage);

        // Act
        Page<Student> resultPage = adapter.findAll(pageable);

        // Assert
        assertThat(resultPage)
                .isNotNull()
                .isEmpty();

        verify(repository, times(1)).findAll(pageable);
    }

    @Test
    @DisplayName("should map entity to domain correctly")
    void shouldMapEntityToDomainCorrectly() {
        // Arrange
        when(repository.save(any(StudentJpaEntity.class))).thenReturn(studentEntity);

        // Act
        Student result = adapter.save(studentDomain);

        // Assert
        assertThat(result)
                .isNotNull()
                .hasFieldOrPropertyWithValue("matricula", studentEntity.getMatricula())
                .hasFieldOrPropertyWithValue("name", studentEntity.getName());
    }

    @Test
    @DisplayName("should save multiple students")
    void shouldSaveMultipleStudents() {
        // Arrange
        when(repository.save(any(StudentJpaEntity.class))).thenReturn(studentEntity);

        // Act
        Student saved1 = adapter.save(studentDomain);
        Student saved2 = adapter.save(studentDomain);

        // Assert
        assertThat(saved1).isNotNull();
        assertThat(saved2).isNotNull();

        verify(repository, times(2)).save(any(StudentJpaEntity.class));
    }

}

