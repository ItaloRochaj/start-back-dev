package start.students.adapters.outbound.persistence.adapters;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import start.students.adapters.outbound.persistence.entities.StudentJpaEntity;
import start.students.adapters.outbound.repositories.StudentJpaRepository;
import start.students.core.domain.entities.Student;
import start.students.core.ports.StudentRepositoryPort;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class StudentPersistenceAdapter implements StudentRepositoryPort {

    private final StudentJpaRepository repository;

    @Override
    public Student save(Student student) {
        StudentJpaEntity entity = toEntity(student);
        StudentJpaEntity savedEntity = repository.save(entity);
        return toDomain(savedEntity);
    }

    @Override
    public Optional<Student> findById(String id) {
        return repository.findById(id).map(this::toDomain);
    }

    @Override
    public void deleteById(String id) {
        repository.deleteById(id);
    }

    @Override
    public Page<Student> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(this::toDomain);
    }

    @Override
    public Page<Student> findByNameContainingIgnoreCase(String name, Pageable pageable) {
        return repository.findByNameContainingIgnoreCase(name, pageable).map(this::toDomain);
    }

    @Override
    public Page<Student> findByCpfContaining(String cpf, Pageable pageable) {
        return repository.findByCpfContaining(cpf, pageable).map(this::toDomain);
    }

    @Override
    public Page<Student> findByEmailContainingIgnoreCase(String email, Pageable pageable) {
        return repository.findByEmailContainingIgnoreCase(email, pageable).map(this::toDomain);
    }

    @Override
    public boolean existsByCpf(String cpf) {
        return repository.existsByCpf(cpf);
    }

    @Override
    public boolean existsByEmail(String email) {
        return repository.existsByEmail(email);
    }

    private StudentJpaEntity toEntity(Student student) {
        return new StudentJpaEntity(
                student.getId(),
                student.getName(),
                student.getCpf(),
                student.getEmail(),
                student.getPhone(),
                student.getPhoto(),
                student.getCreatedAt(),
                student.getUpdatedAt()
        );
    }

    private Student toDomain(StudentJpaEntity entity) {
        return new Student(
                entity.getId(),
                entity.getName(),
                entity.getCpf(),
                entity.getEmail(),
                entity.getPhone(),
                entity.getPhoto(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
