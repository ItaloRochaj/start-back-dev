package start.students.core.ports;

import start.students.core.domain.entities.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface StudentRepositoryPort {
    Student save(Student student);

    Optional<Student> findById(String id);

    void deleteById(String id);

    Page<Student> findAll(Pageable pageable);

    Page<Student> findByNameContainingIgnoreCase(String name, Pageable pageable);

    Page<Student> findByCpfContaining(String cpf, Pageable pageable);

    Page<Student> findByEmailContainingIgnoreCase(String email, Pageable pageable);

    boolean existsByCpf(String cpf);

    boolean existsByEmail(String email);
}
