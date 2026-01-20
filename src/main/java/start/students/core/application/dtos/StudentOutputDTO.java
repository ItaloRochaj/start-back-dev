package start.students.core.application.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class StudentOutputDTO {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public class StudentOutputDTO {
        private String id;
        private String matricula;
        private String nomeCompleto;
        private String email;
        private String cpf;
        private String telefone;
        private String foto;
        private String status;
        private LocalDateTime criadoEm;
        private LocalDateTime atualizadoEm;
    }

}
