package start.students.core.application.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginInputDTO {
    @NotBlank(message = "Username é obrigatório")
    private String username;

    @NotBlank(message = "Password é obrigatório")
    private String password;
}
