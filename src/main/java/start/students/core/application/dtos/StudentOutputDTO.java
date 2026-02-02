package start.students.core.application.dtos;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class StudentOutputDTO {
    private String id;
    private String matricula;
    private String name;
    private String cpf;
    private String email;
    private String phone;
    private String photo;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
