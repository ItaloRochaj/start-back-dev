package start.students.core.application.usecases;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import start.students.core.application.dtos.StudentOutputDTO;
import start.students.core.application.dtos.UpdateStudentInputDTO;
import start.students.core.application.mappers.StudentMapper;
import start.students.core.domain.entities.Student;
import start.students.core.domain.exceptions.StudentNotFoundException;
import start.students.core.domain.valueobjects.Status;
import start.students.core.domain.valueobjects.StudentId;
import start.students.core.ports.StudentRepositoryPort;

public class UpdateStudentUseCase {

    @Service
    @RequiredArgsConstructor
    public class UpdateStudentUseCase {

        private final StudentRepositoryPort studentRepository;
        private final StudentMapper studentMapper;

        public StudentOutputDTO execute(String studentId, UpdateStudentInputDTO input) {
            Student student = studentRepository.findById(new StudentId(studentId))
                    .orElseThrow(() -> new StudentNotFoundException("Aluno não encontrado"));

            Status status = Status.valueOf(input.getStatus().toUpperCase());

            student.atualizar(
                    input.getEmail(),
                    input.getTelefone(),
                    status,
                    input.getFoto()
            );

            Student studentAtualizado = studentRepository.save(student);
            return studentMapper.toOutputDTO(studentAtualizado);
        }

    }
}
