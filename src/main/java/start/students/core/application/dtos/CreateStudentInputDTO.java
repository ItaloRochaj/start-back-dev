package start.students.core.application.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import start.students.core.application.dtos.validators.ValidFullName;
import start.students.core.application.dtos.validators.ValidCPF;

@Data
public class CreateStudentInputDTO {
    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    @ValidFullName(message = "Nome deve ser completo (informe pelo menos nome e sobrenome)")
    private String name;

    @NotBlank(message = "CPF é obrigatório")
    @Size(min = 11, max = 14, message = "CPF deve ter formato válido")
    @ValidCPF(message = "CPF inválido")
    private String cpf;

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email deve ter formato válido")
    private String email;

    private String phone;
    
    @Size(max = 2097152, message = "Foto não pode ser maior que 2MB") // 2MB em base64
    private String photo;
}
