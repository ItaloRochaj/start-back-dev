package start.students.adapters.inbound.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import start.students.adapters.inbound.http.ApiResponse;
import start.students.core.application.dtos.LoginInputDTO;
import start.students.core.application.dtos.TokenResponseDTO;
import start.students.core.domain.entities.User;
import start.students.core.domain.exceptions.DomainException;
import start.students.core.ports.JwtTokenPort;
import start.students.core.ports.PasswordEncoderPort;
import start.students.core.ports.UserRepositoryPort;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepositoryPort userRepository;
    private final PasswordEncoderPort passwordEncoder;
    private final JwtTokenPort jwtTokenPort;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<TokenResponseDTO>> login(@Valid @RequestBody LoginInputDTO input) {
        try {
            User user = userRepository.findByUsername(input.getUsername())
                    .orElseThrow(() -> new DomainException("Credenciais inválidas"));

            if (!passwordEncoder.matches(input.getPassword(), user.getPassword())) {
                throw new DomainException("Credenciais inválidas");
            }

            String token = jwtTokenPort.generateToken(user.getUsername());

            TokenResponseDTO response = TokenResponseDTO.builder()
                    .token(token)
                    .username(user.getUsername())
                    .build();

            return ResponseEntity.ok(ApiResponse.success("Login realizado com sucesso", response));

        } catch (DomainException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}
