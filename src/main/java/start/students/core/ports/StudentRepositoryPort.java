package start.students.core.ports;

import start.students.core.domain.entities.Student;
import start.students.core.application.usecases.ListStudentsUseCase.PaginationRequest;
import start.students.core.application.usecases.ListStudentsUseCase.PaginationResponse;

import java.util.Optional;

public interface StudentRepositoryPort {
    Student save(Student student);

    Optional<Student> findById(String id);

    void deleteById(String id);

    PaginationResponse<Student> findPagedStudents(PaginationRequest request);

    boolean existsByCpf(String cpf);

    boolean existsByEmail(String email);
}
