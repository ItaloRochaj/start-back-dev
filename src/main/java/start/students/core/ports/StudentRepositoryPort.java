package start.students.core.ports;

import start.students.core.domain.entities.Student;
import start.students.core.domain.valueobjects.StudentId;

import java.util.List;
import java.util.Optional;

public class StudentRepositoryPort {

    public interface StudentRepositoryPort {
        Student save(Student student);

        Optional<Student> findById(StudentId id);

        Optional<Student> findByEmail(String email);

        Optional<Student> findByCpf(String cpf);

        List<Student> findAll(int page, int size);

        List<Student> searchByName(String name, int page, int size);

        List<Student> searchByMatricula(String matricula, int page, int size);

        long count();

        void delete(Student student);
    }
}
