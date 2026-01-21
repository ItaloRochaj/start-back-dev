package start.students.core.application.usecases;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import start.students.core.application.dtos.StudentOutputDTO;
import start.students.core.application.mappers.StudentMapper;
import start.students.core.domain.entities.Student;
import start.students.core.domain.exceptions.StudentNotFoundException;
import start.students.core.domain.valueobjects.StudentId;
import start.students.core.ports.StudentRepositoryPort;

public class GetStudentDetailUseCase {

    @Service
    @RequiredArgsConstructor
    public class GetStudentDetailUseCase {

        private final StudentRepositoryPort studentRepository;
        private final StudentMapper studentMapper;

        public StudentOutputDTO execute(String studentId) {
            Student student = studentRepository.findById(new StudentId(studentId))
                    .orElseThrow(() -> new StudentNotFoundException("Aluno não encontrado"));

            if (student.estaDeleted()) {
                throw new StudentNotFoundException("Aluno foi removido");
            }

            return studentMapper.toOutputDTO(student);
        }
    }
}
