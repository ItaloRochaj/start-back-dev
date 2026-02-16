package start.students.core.application.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class LoginInputDTO {
    @NotBlank(message = "Username é obrigatório")
    @Size(min = 8, max = 50, message = "Username deve ter entre 8 e 50 caracteres")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Username deve conter apenas letras (sem acento) e números")
    private String username;

    @NotBlank(message = "Password é obrigatório")
    @Size(min = 8, max = 100, message = "Password deve ter entre 8 e 100 caracteres")
    // Nota: @Pattern removido para aceitar hashes BCrypt que contêm $, ., etc
    // Validação de formato alphanumeric feito apenas no frontend e no UseCase
    private String password;
}
