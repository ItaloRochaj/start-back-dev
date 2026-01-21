package start.students.core.ports;

import start.students.core.domain.entities.User;

import java.util.Optional;

public class UserRepositoryPort {

    public interface UserRepositoryPort {
        Optional<User> findByUsername(String username);

        User save(User user);
    }
}
