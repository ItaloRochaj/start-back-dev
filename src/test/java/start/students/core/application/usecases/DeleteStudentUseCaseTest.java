package start.students.core.application.usecases;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import start.students.core.domain.entities.Student;
import start.students.core.domain.exceptions.StudentNotFoundException;
import start.students.core.ports.StudentRepositoryPort;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("DeleteStudentUseCase Tests")
class DeleteStudentUseCaseTest {

    @Mock
    private StudentRepositoryPort studentRepository;

    @InjectMocks
    private DeleteStudentUseCase useCase;

    @Test
    @DisplayName("Should delete student successfully")
    void shouldDeleteStudentSuccessfully() {
        // ARRANGE
        String studentId = "uuid-1";
        Student student = new Student();
        student.setId("uuid-1");

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        doNothing().when(studentRepository).deleteById(studentId);

        // ACT
        useCase.execute(studentId);

        // ASSERT
        verify(studentRepository, times(1)).findById(studentId);
        verify(studentRepository, times(1)).deleteById(studentId);
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
        verify(studentRepository, never()).deleteById(any());
    }
}

