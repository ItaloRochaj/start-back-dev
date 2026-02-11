package start.students.core.application.mappers;

import start.students.core.application.dtos.CreateStudentInputDTO;
import start.students.core.application.dtos.StudentOutputDTO;
import start.students.core.application.dtos.UpdateStudentInputDTO;
import start.students.core.domain.entities.Student;

import java.time.LocalDateTime;
import java.util.UUID;

public class StudentMapper {

    // Dependência para gerar matrícula - será injetada via construtor
    private final MatriculaService matriculaService;

    public StudentMapper(MatriculaService matriculaService) {
        this.matriculaService = matriculaService;
    }

    public Student toEntity(CreateStudentInputDTO dto) {
        Student student = new Student();
        student.setId(UUID.randomUUID().toString());
        student.setMatricula(matriculaService.generateMatricula());
        student.setName(dto.getName());
        student.setCpf(dto.getCpf());
        student.setEmail(dto.getEmail());
        student.setPhone(dto.getPhone());
        student.setPhoto(dto.getPhoto());
        student.setStatus("Ativo");
        student.setCreatedAt(LocalDateTime.now());
        student.setUpdatedAt(LocalDateTime.now());
        return student;
    }

    public StudentOutputDTO toOutputDTO(Student student) {
        return StudentOutputDTO.builder()
                .id(student.getId())
                .matricula(student.getMatricula())
                .name(student.getName())
                .cpf(student.getCpf())
                .email(student.getEmail())
                .phone(student.getPhone())
                .photo(student.getPhoto())
                .status(student.getStatus())
                .createdAt(student.getCreatedAt())
                .updatedAt(student.getUpdatedAt())
                .build();
    }

    public void updateEntityFromDTO(UpdateStudentInputDTO dto, Student student) {
        if (dto.getName() != null && !dto.getName().trim().isEmpty()) {
            student.setName(dto.getName());
        }
        if (dto.getCpf() != null && !dto.getCpf().trim().isEmpty()) {
            student.setCpf(dto.getCpf());
        }
        if (dto.getEmail() != null && !dto.getEmail().trim().isEmpty()) {
            student.setEmail(dto.getEmail());
        }
        if (dto.getPhone() != null && !dto.getPhone().trim().isEmpty()) {
            student.setPhone(dto.getPhone());
        }
        if (dto.getPhoto() != null && !dto.getPhoto().trim().isEmpty()) {
            student.setPhoto(dto.getPhoto());
        }
        if (dto.getStatus() != null && !dto.getStatus().trim().isEmpty()) {
            student.setStatus(dto.getStatus());
        }
        student.setUpdatedAt(LocalDateTime.now());
    }

    /**
     * Interface que deve ser implementada pelos adapters
     * Remove a necessidade de injetar repositório diretamente no core
     */
    public interface MatriculaService {
        String generateMatricula();
    }
}
