package start.students.core.application.usecases;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import start.students.core.domain.exceptions.StudentNotFoundException;
import start.students.core.ports.StudentRepositoryPort;

@Service
@RequiredArgsConstructor
public class DeleteStudentUseCase {

    private final StudentRepositoryPort studentRepository;

    public void execute(String id) {
        if (!studentRepository.findById(id).isPresent()) {
            throw new StudentNotFoundException("Estudante não encontrado");
        }

        studentRepository.deleteById(id);
    }
}
