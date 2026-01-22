import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GenerateBcryptHash {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String password = "admin1234";
        String hash = encoder.encode(password);
        System.out.println("Senha: " + password);
        System.out.println("Hash BCrypt: " + hash);
    }
}
