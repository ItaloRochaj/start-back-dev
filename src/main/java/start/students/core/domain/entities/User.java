package start.students.core.domain.entities;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private String id;
    
    @Column(unique = true, nullable = false)
    @Size(min = 8, max = 50, message = "Username deve ter entre 8 e 50 caracteres")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Username deve conter apenas letras e números")
    private String username;
    
    @Column(nullable = false)
    @Size(min = 8, max = 100, message = "Password deve ter entre 8 e 100 caracteres")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Password deve conter apenas letras e números")
    private String password;
    
    private String email;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}