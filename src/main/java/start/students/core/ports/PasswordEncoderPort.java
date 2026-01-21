package start.students.core.ports;

public class PasswordEncoderPort {

    public interface PasswordEncoderPort {
        String encode(String rawPassword);
        boolean matches(String rawPassword, String encodedPassword);
    }
}
