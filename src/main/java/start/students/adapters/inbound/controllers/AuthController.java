package start.students.adapters.inbound.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import start.students.adapters.inbound.http.ApiResponse;
import start.students.core.application.dtos.LoginInputDTO;
import start.students.core.application.dtos.TokenResponseDTO;
import start.students.core.application.usecases.AuthenticationUseCase;
import start.students.core.domain.exceptions.DomainException;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationUseCase authenticationUseCase;

    public AuthController(AuthenticationUseCase authenticationUseCase) {
        this.authenticationUseCase = authenticationUseCase;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<TokenResponseDTO>> login(@RequestBody LoginInputDTO input) {
        try {
            TokenResponseDTO response = authenticationUseCase.authenticate(input);
            return ResponseEntity.ok(ApiResponse.success("Login realizado com sucesso", response));
        } catch (DomainException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}
