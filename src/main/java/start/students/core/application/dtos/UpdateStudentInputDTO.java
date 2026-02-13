package start.students.core.application.dtos;

import lombok.Data;

@Data
public class UpdateStudentInputDTO {
    private String name;
    private String cpf;
    private String email;
    private String phone;
    
    private String photo;

    private String status;
}
