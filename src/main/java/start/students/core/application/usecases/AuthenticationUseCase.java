package start.students.core.application.usecases;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import start.students.core.application.dtos.LoginInputDTO;
import start.students.core.application.dtos.TokenResponseDTO;
import start.students.core.domain.entities.User;
import start.students.core.domain.exceptions.DomainException;
import start.students.core.ports.JwtTokenPort;
import start.students.core.ports.PasswordEncoderPort;
import start.students.core.ports.UserRepositoryPort;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationUseCase {

    private final UserRepositoryPort userRepository;
    private final PasswordEncoderPort passwordEncoder;
    private final JwtTokenPort jwtTokenPort;

    public TokenResponseDTO authenticate(LoginInputDTO input) {
        log.info("Tentativa de login para usuário: {}", input.getUsername());
        
        // Validação defensiva: verificar formato alphanumeric
        if (!isValidAlphanumeric(input.getUsername()) || !isValidAlphanumeric(input.getPassword())) {
            log.warn("Tentativa de login com formato inválido. Username: {}", input.getUsername());
            throw new DomainException("Username e Password devem conter apenas letras (sem acento) e números");
        }
        
        // Buscar usuário por username
        User user = userRepository.findByUsername(input.getUsername())
                .orElseThrow(() -> {
                    log.warn("Usuário não encontrado: {}", input.getUsername());
                    return new DomainException("Credenciais inválidas");
                });

        log.info("Usuário encontrado: {} - Hash da senha: {}", user.getUsername(), 
                 user.getPassword().substring(0, Math.min(20, user.getPassword().length())) + "...");

        // Verificar senha
        boolean passwordMatch = passwordEncoder.matches(input.getPassword(), user.getPassword());
        log.info("Senha inserida: {} - Match: {}", input.getPassword(), passwordMatch);
        
        if (!passwordMatch) {
            log.warn("Senha incorreta para usuário: {}", input.getUsername());
            throw new DomainException("Credenciais inválidas");
        }

        log.info("Login bem-sucedido para usuário: {}", user.getUsername());
        
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
