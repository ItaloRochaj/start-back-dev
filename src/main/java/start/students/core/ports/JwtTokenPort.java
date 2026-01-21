package start.students.core.ports;

public interface JwtTokenPort {
    String generateToken(String username);
    String extractUsername(String token);
    boolean isTokenValid(String token, String username);
}
