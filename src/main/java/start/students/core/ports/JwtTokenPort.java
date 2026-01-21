package start.students.core.ports;

public class JwtTokenPort {

    public interface JwtTokenPort {
        String generateToken(String username);
        boolean validateToken(String token);
        String getUsernameFromToken(String token);
    }
}
