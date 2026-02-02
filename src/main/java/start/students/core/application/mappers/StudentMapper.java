package start.students.core.application.mappers;

import org.springframework.stereotype.Component;
import start.students.core.application.dtos.CreateStudentInputDTO;
import start.students.core.application.dtos.StudentOutputDTO;
import start.students.core.application.dtos.UpdateStudentInputDTO;
import start.students.core.domain.entities.Student;

import java.time.LocalDateTime;
import java.time.Year;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class StudentMapper {
    
    // Contador sequencial para matrícula (em produção, usar banco de dados)
    private static final AtomicLong matriculaCounter = new AtomicLong(1);

    public Student toEntity(CreateStudentInputDTO dto) {
        Student student = new Student();
        student.setId(UUID.randomUUID().toString());
        student.setMatricula(generateMatricula());
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
     * Gera matrícula no formato YYYY + sequência (ex: 2026000001)
     */
    private String generateMatricula() {
        int currentYear = Year.now().getValue();
        long sequence = matriculaCounter.getAndIncrement();
        return String.format("%d%05d", currentYear, sequence);
    }
}
