package start.students.adapters.inbound.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/debug")
@RequiredArgsConstructor
public class PasswordEncoderController {

    private final PasswordEncoder passwordEncoder;

    /**
     * Endpoint para gerar hash BCrypt de uma senha
     * Útil para testes e debug
     * Exemplo: GET http://localhost:8080/debug/encode-password?password=admin1234
     */
    @GetMapping("/encode-password")
    public String encodePassword(@RequestParam String password) {
        return passwordEncoder.encode(password);
    }

    /**
     * Endpoint para verificar se uma senha corresponde a um hash
     * Exemplo: POST http://localhost:8080/debug/verify-password?password=admin1234&hash=$2a$10$...
     */
    @PostMapping("/verify-password")
    public boolean verifyPassword(@RequestParam String password, @RequestParam String hash) {
        return passwordEncoder.matches(password, hash);
    }
}
