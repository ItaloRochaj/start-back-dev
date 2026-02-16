package start.students.core.application.usecases;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import start.students.core.application.dtos.StudentOutputDTO;
import start.students.core.application.dtos.UpdateStudentInputDTO;
import start.students.core.application.mappers.StudentMapper;
import start.students.core.domain.entities.Student;
import start.students.core.domain.exceptions.DomainException;
import start.students.core.domain.exceptions.StudentNotFoundException;
import start.students.core.ports.StudentRepositoryPort;

@Service
@RequiredArgsConstructor
public class UpdateStudentUseCase {

    private final StudentRepositoryPort studentRepository;
    private final StudentMapper studentMapper;

    public StudentOutputDTO execute(String id, UpdateStudentInputDTO input) {
        Student existingStudent = studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException("Estudante não encontrado"));

        // Validar CPF se foi alterado
        if (input.getCpf() != null && !input.getCpf().equals(existingStudent.getCpf())
            && studentRepository.existsByCpf(input.getCpf())) {
            throw new DomainException("CPF já está cadastrado");
        }

        // Validar email se foi alterado
        if (input.getEmail() != null && !input.getEmail().equalsIgnoreCase(existingStudent.getEmail())
            && studentRepository.existsByEmailIgnoreCase(input.getEmail())) {
            throw new DomainException("Email já está cadastrado");
        }

        // Atualizar campos não nulos
        studentMapper.updateEntityFromDTO(input, existingStudent);

        // Salvar alterações
        Student updatedStudent = studentRepository.save(existingStudent);

        return studentMapper.toOutputDTO(updatedStudent);
    }
}
