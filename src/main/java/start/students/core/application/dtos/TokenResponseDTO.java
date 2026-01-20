package start.students.core.application.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class TokenResponseDTO {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public class TokenResponseDTO {
        private String token;
        private String type;
        private Long expiresIn;
    }

}
