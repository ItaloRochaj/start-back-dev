package start.students.core.application.usecases;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import start.students.core.application.dtos.StudentOutputDTO;
import start.students.core.application.dtos.StudentPagedOutputDTO;
import start.students.core.application.mappers.StudentMapper;
import start.students.core.domain.entities.Student;
import start.students.core.ports.StudentRepositoryPort;

public class ListStudentsUseCase {

    Service
    @RequiredArgsConstructor
    public class ListStudentsUseCase {

        private final StudentRepositoryPort studentRepository;
        private final StudentMapper studentMapper;

        public StudentPagedOutputDTO execute(int page, int size, String search, String searchType) {
            page = Math.max(0, page - 1);

            List<Student> students;
            long totalElements;

            if (search != null && !search.isBlank()) {
                if ("matricula".equalsIgnoreCase(searchType)) {
                    students = studentRepository.searchByMatricula(search, page, size);
                } else {
                    students = studentRepository.searchByName(search, page, size);
                }
                totalElements = students.size();
            } else {
                students = studentRepository.findAll(page, size);
                totalElements = studentRepository.count();
            }

            List<StudentOutputDTO> content = students.stream()
                    .filter(s -> !s.estaDeleted())
                    .map(studentMapper::toOutputDTO)
                    .toList();

            int totalPages = (int) Math.ceil((double) totalElements / size);

            return StudentPagedOutputDTO.builder()
                    .content(content)
                    .pageNumber(page + 1)
                    .pageSize(size)
                    .totalElements(totalElements)
                    .totalPages(totalPages)
                    .hasNext(page + 1 < totalPages)
                    .hasPrevious(page > 0)
                    .build();
        }
    }
}
