package start.students.core.application.usecases;

import start.students.core.application.dtos.CreateStudentInputDTO;
import start.students.core.application.dtos.StudentOutputDTO;
import start.students.core.application.mappers.StudentMapper;
import start.students.core.domain.entities.Student;
import start.students.core.domain.exceptions.DomainException;
import start.students.core.ports.StudentRepositoryPort;

public class CreateStudentUseCase {

    private final StudentRepositoryPort studentRepository;
    private final StudentMapper studentMapper;

    public CreateStudentUseCase(StudentRepositoryPort studentRepository, StudentMapper studentMapper) {
        this.studentRepository = studentRepository;
        this.studentMapper = studentMapper;
    }

    public StudentOutputDTO execute(CreateStudentInputDTO input) {
        // Validar se CPF já existe
        if (studentRepository.existsByCpf(input.getCpf())) {
            throw new DomainException("CPF já está cadastrado");
        }

        // Validar se email já existe
        if (studentRepository.existsByEmail(input.getEmail())) {
            throw new DomainException("Email já está cadastrado");
        }

        // Converter DTO para entidade
        Student student = studentMapper.toEntity(input);

        // Salvar no repositório
        Student savedStudent = studentRepository.save(student);

        // Converter entidade para DTO de saída
        return studentMapper.toOutputDTO(savedStudent);
    }
}
