package start.students.core.application.usecases;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import start.students.core.domain.entities.Student;
import start.students.core.domain.exceptions.StudentNotFoundException;
import start.students.core.domain.valueobjects.StudentId;
import start.students.core.ports.StudentRepositoryPort;

public class DeleteStudentUseCase {
    @Service
    @RequiredArgsConstructor
    public class DeleteStudentUseCase {

        private final StudentRepositoryPort studentRepository;

        public void execute(String studentId) {
            Student student = studentRepository.findById(new StudentId(studentId))
                    .orElseThrow(() -> new StudentNotFoundException("Aluno não encontrado"));

            student.deletar();
            studentRepository.save(student);
        }
    }
}
