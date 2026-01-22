package start.students.core.application.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenResponseDTO {
    private String token;
    private String username;
    @Builder.Default
    private String tokenType = "Bearer";
}
