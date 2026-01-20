package start.students.core.application.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

public class StudentPagedOutputDTO {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public class StudentPagedOutputDTO {
        private List<StudentOutputDTO> content;
        private int pageNumber;
        private int pageSize;
        private long totalElements;
        private int totalPages;
        private boolean hasNext;
        private boolean hasPrevious;
    }

}
