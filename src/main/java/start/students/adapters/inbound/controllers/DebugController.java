package start.students.adapters.inbound.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import start.students.core.ports.PasswordEncoderPort;
import start.students.core.ports.UserRepositoryPort;
import start.students.core.domain.entities.User;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/debug")
@RequiredArgsConstructor
public class DebugController {

    private final PasswordEncoderPort passwordEncoder;
    private final UserRepositoryPort userRepository;

    @PostMapping("/test-login")
    public ResponseEntity<Map<String, Object>> testLogin() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // 1. Verificar se o usuário existe
            var userOpt = userRepository.findByUsername("admin");
            if (!userOpt.isPresent()) {
                response.put("error", "Usuário não encontrado");
                return ResponseEntity.ok(response);
            }
            
            User user = userOpt.get();
            response.put("userFound", true);
            response.put("username", user.getUsername());
            response.put("passwordFromDB", user.getPassword());
            response.put("passwordLength", user.getPassword().length());
            
            // 2. Testar diferentes senhas
            String[] testPasswords = {"123456", "admin123", "password", "admin"};
            Map<String, Boolean> passwordTests = new HashMap<>();
            
            for (String testPassword : testPasswords) {
                boolean matches = passwordEncoder.matches(testPassword, user.getPassword());
                passwordTests.put(testPassword, matches);
            }
            response.put("passwordTests", passwordTests);
            
            // 3. Gerar novo hash para 123456
            String newHash = passwordEncoder.encode("123456");
            response.put("newHashFor123456", newHash);
            response.put("newHashLength", newHash.length());
            
            // 4. Testar o novo hash
            boolean newHashTest = passwordEncoder.matches("123456", newHash);
            response.put("newHashValidation", newHashTest);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("error", e.getMessage());
            response.put("stackTrace", e.getStackTrace()[0].toString());
            return ResponseEntity.ok(response);
        }
    }
    
    @PostMapping("/fix-password")
    public ResponseEntity<Map<String, Object>> fixPassword() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Buscar usuário
            var userOpt = userRepository.findByUsername("admin");
            if (!userOpt.isPresent()) {
                response.put("error", "Usuário não encontrado");
                return ResponseEntity.ok(response);
            }
            
            User user = userOpt.get();
            
            // Gerar nova senha
            String newPassword = passwordEncoder.encode("123456");
            user.setPassword(newPassword);
            
            // Salvar
            userRepository.save(user);
            
            response.put("success", true);
            response.put("message", "Senha atualizada com sucesso");
            response.put("newPassword", newPassword);
            response.put("testValidation", passwordEncoder.matches("123456", newPassword));
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("error", e.getMessage());
            return ResponseEntity.ok(response);
        }
    }
}
