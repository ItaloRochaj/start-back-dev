package start.students.core.application.usecases;

import start.students.core.domain.exceptions.StudentNotFoundException;
import start.students.core.ports.StudentRepositoryPort;

public class DeleteStudentUseCase {

    private final StudentRepositoryPort studentRepository;

    public DeleteStudentUseCase(StudentRepositoryPort studentRepository) {
        this.studentRepository = studentRepository;
    }

    public void execute(String id) {
        if (!studentRepository.findById(id).isPresent()) {
            throw new StudentNotFoundException("Estudante não encontrado");
        }

        studentRepository.deleteById(id);
    }
}
