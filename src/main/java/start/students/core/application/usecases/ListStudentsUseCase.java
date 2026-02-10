package start.students.core.application.usecases;

import start.students.core.application.dtos.StudentPagedOutputDTO;
import start.students.core.application.mappers.StudentMapper;
import start.students.core.domain.entities.Student;
import start.students.core.ports.StudentRepositoryPort;

import java.util.stream.Collectors;

public class ListStudentsUseCase {

    private final StudentRepositoryPort studentRepository;
    private final StudentMapper studentMapper;

    public ListStudentsUseCase(StudentRepositoryPort studentRepository, StudentMapper studentMapper) {
        this.studentRepository = studentRepository;
        this.studentMapper = studentMapper;
    }

    public StudentPagedOutputDTO execute(int page, int size, String search, String searchType) {
        // Converter página/tamanho em PaginationRequest (será implementado no adapter)
        PaginationResponse<Student> response = studentRepository.findPagedStudents(
            new PaginationRequest(page, size, search, searchType)
        );

        return StudentPagedOutputDTO.builder()
                .content(response.getContent().stream()
                        .map(studentMapper::toOutputDTO)
                        .collect(Collectors.toList()))
                .page(response.getPageNumber())
                .size(response.getPageSize())
                .totalElements(response.getTotalElements())
                .totalPages(response.getTotalPages())
                .first(response.isFirst())
                .last(response.isLast())
                .build();
    }

    public boolean validateCpfExists(String cpf) {
        return studentRepository.existsByCpf(cpf);
    }

    public boolean validateEmailExists(String email) {
        return studentRepository.existsByEmail(email);
    }

    // Classes internas para abstração de paginação
    public static class PaginationRequest {
        private final int page;
        private final int size;
        private final String search;
        private final String searchType;

        public PaginationRequest(int page, int size, String search, String searchType) {
            this.page = page;
            this.size = size;
            this.search = search;
            this.searchType = searchType;
        }

        public int getPage() { return page; }
        public int getSize() { return size; }
        public String getSearch() { return search; }
        public String getSearchType() { return searchType; }
    }

    public static class PaginationResponse<T> {
        private final java.util.List<T> content;
        private final int pageNumber;
        private final int pageSize;
        private final long totalElements;
        private final int totalPages;
        private final boolean first;
        private final boolean last;

        public PaginationResponse(java.util.List<T> content, int pageNumber, int pageSize, 
                                   long totalElements, int totalPages, boolean first, boolean last) {
            this.content = content;
            this.pageNumber = pageNumber;
            this.pageSize = pageSize;
            this.totalElements = totalElements;
            this.totalPages = totalPages;
            this.first = first;
            this.last = last;
        }

        public java.util.List<T> getContent() { return content; }
        public int getPageNumber() { return pageNumber; }
        public int getPageSize() { return pageSize; }
        public long getTotalElements() { return totalElements; }
        public int getTotalPages() { return totalPages; }
        public boolean isFirst() { return first; }
        public boolean isLast() { return last; }
    }
}
