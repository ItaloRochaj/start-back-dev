package start.students.adapters.outbound.persistence.adapters;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import start.students.adapters.outbound.persistence.entities.StudentJpaEntity;
import start.students.adapters.outbound.repositories.StudentJpaRepository;
import start.students.core.application.usecases.ListStudentsUseCase.PaginationRequest;
import start.students.core.application.usecases.ListStudentsUseCase.PaginationResponse;
import start.students.core.domain.entities.Student;
import start.students.core.ports.StudentRepositoryPort;

import java.time.Year;
import java.util.Optional;

public class StudentPersistenceAdapter implements StudentRepositoryPort {

    private final StudentJpaRepository repository;

    public StudentPersistenceAdapter(StudentJpaRepository repository) {
        this.repository = repository;
    }

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
    public PaginationResponse<Student> findPagedStudents(PaginationRequest request) {
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        Page<Student> studentsPage;

        if (request.getSearch() == null || request.getSearch().trim().isEmpty()) {
            studentsPage = repository.findAll(pageable).map(this::toDomain);
        } else {
            switch (request.getSearchType().toLowerCase()) {
                case "cpf":
                    studentsPage = repository.findByCpfContaining(request.getSearch(), pageable).map(this::toDomain);
                    break;
                case "email":
                    studentsPage = repository.findByEmailContainingIgnoreCase(request.getSearch(), pageable).map(this::toDomain);
                    break;
                case "matricula":
                    studentsPage = repository.findByMatriculaContaining(request.getSearch(), pageable).map(this::toDomain);
                    break;
                case "name":
                default:
                    studentsPage = repository.findByNameContainingIgnoreCase(request.getSearch(), pageable).map(this::toDomain);
                    break;
            }
        }

        return new PaginationResponse<>(
                studentsPage.getContent(),
                studentsPage.getNumber(),
                studentsPage.getSize(),
                studentsPage.getTotalElements(),
                studentsPage.getTotalPages(),
                studentsPage.isFirst(),
                studentsPage.isLast()
        );
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
                student.getMatricula(),
                student.getName(),
                student.getCpf(),
                student.getEmail(),
                student.getPhone(),
                student.getPhoto(),
                student.getStatus(),
                student.getCreatedAt(),
                student.getUpdatedAt()
        );
    }

    private Student toDomain(StudentJpaEntity entity) {
        return new Student(
                entity.getId(),
                entity.getMatricula(),
                entity.getName(),
                entity.getCpf(),
                entity.getEmail(),
                entity.getPhone(),
                entity.getPhoto(),
                entity.getStatus(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
