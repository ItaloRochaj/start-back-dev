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
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestPasswordController {

    private final PasswordEncoderPort passwordEncoder;
    private final UserRepositoryPort userRepository;

    @PostMapping("/generate-password")
    public ResponseEntity<Map<String, String>> generatePassword(@RequestParam String password) {
        String encoded = passwordEncoder.encode(password);
        Map<String, String> response = new HashMap<>();
        response.put("originalPassword", password);
        response.put("encodedPassword", encoded);
        response.put("length", String.valueOf(encoded.length()));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/validate-password")
    public ResponseEntity<Map<String, Object>> validatePassword(
            @RequestParam String rawPassword,
            @RequestParam String encodedPassword) {
        
        boolean matches = passwordEncoder.matches(rawPassword, encodedPassword);
        Map<String, Object> response = new HashMap<>();
        response.put("rawPassword", rawPassword);
        response.put("encodedPassword", encodedPassword);
        response.put("matches", matches);
        response.put("encodedLength", encodedPassword.length());
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/check-user/{username}")
    public ResponseEntity<Map<String, Object>> checkUser(@PathVariable String username) {
        var userOpt = userRepository.findByUsername(username);
        Map<String, Object> response = new HashMap<>();
        
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            response.put("found", true);
            response.put("username", user.getUsername());
            response.put("email", user.getEmail());
            response.put("passwordLength", user.getPassword().length());
            response.put("passwordPrefix", user.getPassword().substring(0, Math.min(10, user.getPassword().length())));
        } else {
            response.put("found", false);
            response.put("message", "Usuário não encontrado");
        }
        
        return ResponseEntity.ok(response);
    }
}
