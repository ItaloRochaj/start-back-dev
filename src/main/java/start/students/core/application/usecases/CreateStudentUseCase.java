package start.students.core.application.usecases;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import start.students.core.application.dtos.CreateStudentInputDTO;
import start.students.core.application.dtos.StudentOutputDTO;
import start.students.core.application.mappers.StudentMapper;
import start.students.core.domain.entities.Student;
import start.students.core.domain.exceptions.DomainException;
import start.students.core.ports.StudentRepositoryPort;

import java.util.UUID;

public class CreateStudentUseCase {
    @Service
    @RequiredArgsConstructor
    public class CreateStudentUseCase {

        private final StudentRepositoryPort studentRepository;
        private final StudentMapper studentMapper;

        public StudentOutputDTO execute(CreateStudentInputDTO input) {
            if (studentRepository.findByEmail(input.getEmail()).isPresent()) {
                throw new DomainException("Email já cadastrado");
            }

            if (studentRepository.findByCpf(input.getCpf()).isPresent()) {
                throw new DomainException("CPF já cadastrado");
            }

            String matricula = "MAT" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

            Student student = Student.criar(
                    matricula,
                    input.getNomeCompleto(),
                    input.getEmail(),
                    input.getCpf(),
                    input.getTelefone(),
                    input.getFoto()
            );

            Student studentSalvo = studentRepository.save(student);
            return studentMapper.toOutputDTO(studentSalvo);
        }

    }
}
