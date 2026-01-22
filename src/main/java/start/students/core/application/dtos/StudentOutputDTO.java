package start.students.core.application.dtos;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class StudentOutputDTO {
    private String id;
    private String name;
    private String cpf;
    private String email;
    private String phone;
    private String photo;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
