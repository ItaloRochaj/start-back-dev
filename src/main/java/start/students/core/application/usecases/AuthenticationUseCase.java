package start.students.core.application.usecases;

import start.students.core.application.dtos.LoginInputDTO;
import start.students.core.application.dtos.TokenResponseDTO;
import start.students.core.domain.entities.User;
import start.students.core.domain.exceptions.DomainException;
import start.students.core.ports.JwtTokenPort;
import start.students.core.ports.PasswordEncoderPort;
import start.students.core.ports.UserRepositoryPort;

public class AuthenticationUseCase {

    private final UserRepositoryPort userRepository;
    private final PasswordEncoderPort passwordEncoder;
    private final JwtTokenPort jwtTokenPort;

    public AuthenticationUseCase(UserRepositoryPort userRepository, 
                                  PasswordEncoderPort passwordEncoder, 
                                  JwtTokenPort jwtTokenPort) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenPort = jwtTokenPort;
    }

    public TokenResponseDTO authenticate(LoginInputDTO input) {
        
        // Validação defensiva: verificar formato alphanumeric
        if (!isValidAlphanumeric(input.getUsername()) || !isValidAlphanumeric(input.getPassword())) {
            throw new DomainException("Username e Password devem conter apenas letras (sem acento) e números");
        }
        
        // Buscar usuário por username
        User user = userRepository.findByUsername(input.getUsername())
                .orElseThrow(() -> new DomainException("Credenciais inválidas"));

        // Verificar senha
        boolean passwordMatch = passwordEncoder.matches(input.getPassword(), user.getPassword());
        
        if (!passwordMatch) {
            throw new DomainException("Credenciais inválidas");
        }
        
        // Gerar token
        String token = jwtTokenPort.generateToken(user.getUsername());

        // Retornar resposta
        return TokenResponseDTO.builder()
                .token(token)
                .username(user.getUsername())
                .build();
    }

    /**
     * Valida se o valor contém apenas letras (a-z, A-Z) e números (0-9)
     * Rejeita: espaços, acentos, cedilha, caracteres especiais
     */
    private boolean isValidAlphanumeric(String value) {
        return value != null && value.matches("^[a-zA-Z0-9]+$");
    }
}
