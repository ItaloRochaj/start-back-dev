package start.students.core.application.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import start.students.adapters.outbound.repositories.StudentJpaRepository;
import start.students.core.application.dtos.CreateStudentInputDTO;
import start.students.core.application.dtos.StudentOutputDTO;
import start.students.core.application.dtos.UpdateStudentInputDTO;
import start.students.core.domain.entities.Student;

import java.time.LocalDateTime;
import java.time.Year;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class StudentMapper {

    private final StudentJpaRepository studentJpaRepository;

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
     * Consulta o banco de dados para obter o próximo número sequencial
     */
    private String generateMatricula() {
        int currentYear = Year.now().getValue();
        Long maxMatricula = studentJpaRepository.findMaxMatricula();
        
        long sequence;
        if (maxMatricula == null) {
            // Primeira matrícula do ano
            sequence = 1;
        } else {
            // Extrair sequência da matrícula anterior e incrementar
            String maxMatriculaStr = String.valueOf(maxMatricula);
            if (maxMatriculaStr.startsWith(String.valueOf(currentYear))) {
                // Matrícula do ano atual, incrementar sequência
                sequence = maxMatricula % 100000 + 1;
            } else {
                // Matrícula de ano anterior, começar nova sequência
                sequence = 1;
            }
        }
        
        return String.format("%d%05d", currentYear, sequence);
    }
}
