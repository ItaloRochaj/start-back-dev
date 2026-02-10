package start.students.core.application.usecases;

import start.students.core.application.dtos.StudentOutputDTO;
import start.students.core.application.mappers.StudentMapper;
import start.students.core.domain.entities.Student;
import start.students.core.domain.exceptions.StudentNotFoundException;
import start.students.core.ports.StudentRepositoryPort;

public class GetStudentDetailUseCase {

    private final StudentRepositoryPort studentRepository;
    private final StudentMapper studentMapper;

    public GetStudentDetailUseCase(StudentRepositoryPort studentRepository, StudentMapper studentMapper) {
        this.studentRepository = studentRepository;
        this.studentMapper = studentMapper;
    }

    public StudentOutputDTO execute(String id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException("Estudante não encontrado"));

        return studentMapper.toOutputDTO(student);
    }
}
