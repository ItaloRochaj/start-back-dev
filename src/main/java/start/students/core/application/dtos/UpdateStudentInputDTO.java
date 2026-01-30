package start.students.core.application.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateStudentInputDTO {
    @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    private String name;

    @Size(min = 11, max = 14, message = "CPF deve ter formato válido")
    private String cpf;

    @Email(message = "Email deve ter formato válido")
    private String email;

    private String phone;
    
    private String photo;

    private String status;
}
