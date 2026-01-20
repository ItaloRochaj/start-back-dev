package start.students.core.application.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class CreateStudentInputDTO {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public class CreateStudentInputDTO {

        @NotBlank(message = "Nome completo é obrigatório")
        @Size(min = 3, max = 100, message = "Nome deve ter entre 3 e 100 caracteres")
        private String nomeCompleto;

        @NotBlank(message = "Email é obrigatório")
        @Email(message = "Email deve ser válido")
        private String email;

        @NotBlank(message = "CPF é obrigatório")
        private String cpf;

        @NotBlank(message = "Telefone é obrigatório")
        @Size(min = 10, max = 15, message = "Telefone inválido")
        private String telefone;

        private String foto;

    }
}
