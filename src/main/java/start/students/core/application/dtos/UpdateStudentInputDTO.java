package start.students.core.application.dtos;

import jakarta.validation.constraints.Size;
import lombok.Data;
import start.students.core.application.dtos.validators.ValidPhone;
import start.students.core.application.dtos.validators.ValidEmail;

@Data
public class UpdateStudentInputDTO {
    @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    private String name;

    @Size(min = 11, max = 14, message = "CPF deve ter formato válido")
    private String cpf;

    @ValidEmail(message = "Email deve conter apenas caracteres ASCII válidos (sem acentos ou cedilha)")
    private String email;

    @ValidPhone
    private String phone;
    
    private String photo;

    private String status;
}
