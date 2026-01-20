package start.students.core.application.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class UpdateStudentInputDTO {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public class UpdateStudentInputDTO {

        @NotBlank(message = "Email é obrigatório")
        @Email(message = "Email deve ser válido")
        private String email;

        @NotBlank(message = "Telefone é obrigatório")
        private String telefone;

        @NotNull(message = "Status é obrigatório")
        private String status;

        private String foto;

    }
}
