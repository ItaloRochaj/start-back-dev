package start.students.core.application.usecases;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import start.students.core.application.dtos.StudentPagedOutputDTO;
import start.students.core.application.mappers.StudentMapper;
import start.students.core.domain.entities.Student;
import start.students.core.ports.StudentRepositoryPort;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ListStudentsUseCase {

    private final StudentRepositoryPort studentRepository;
    private final StudentMapper studentMapper;

    public StudentPagedOutputDTO execute(int page, int size, String search, String searchType) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Student> studentsPage;

        if (search == null || search.trim().isEmpty()) {
            studentsPage = studentRepository.findAll(pageable);
        } else {
            switch (searchType.toLowerCase()) {
                case "cpf":
                    studentsPage = studentRepository.findByCpfContaining(search, pageable);
                    break;
                case "email":
                    studentsPage = studentRepository.findByEmailContainingIgnoreCase(search, pageable);
                    break;
                case "matricula":
                    studentsPage = studentRepository.findByMatriculaContaining(search, pageable);
                    break;
                case "name":
                default:
                    studentsPage = studentRepository.findByNameContainingIgnoreCase(search, pageable);
                    break;
            }
        }

        return StudentPagedOutputDTO.builder()
                .content(studentsPage.getContent().stream()
                        .map(studentMapper::toOutputDTO)
                        .collect(Collectors.toList()))
                .page(studentsPage.getNumber())
                .size(studentsPage.getSize())
                .totalElements(studentsPage.getTotalElements())
                .totalPages(studentsPage.getTotalPages())
                .first(studentsPage.isFirst())
                .last(studentsPage.isLast())
                .build();
    }

    public boolean validateCpfExists(String cpf) {
        return studentRepository.existsByCpf(cpf);
    }

    public boolean validateEmailExists(String email) {
        return studentRepository.existsByEmail(email);
    }
}
