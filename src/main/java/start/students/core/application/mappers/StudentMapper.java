package start.students.core.application.mappers;

import org.springframework.stereotype.Component;
import start.students.core.application.dtos.StudentOutputDTO;
import start.students.core.domain.entities.Student;

public class StudentMapper {

    @Component
    public class StudentMapper {

        public StudentOutputDTO toOutputDTO(Student student) {
            return StudentOutputDTO.builder()
                    .id(student.getId().getValue())
                    .matricula(student.getMatricula())
                    .nomeCompleto(student.getNomeCompleto())
                    .email(student.getEmail().getValue())
                    .cpf(student.getCpf().getValue())
                    .telefone(student.getTelefone())
                    .foto(student.getFoto())
                    .status(student.getStatus().getLabel())
                    .criadoEm(student.getCriadoEm())
                    .atualizadoEm(student.getAtualizadoEm())
                    .build();
            }
        }
    }

