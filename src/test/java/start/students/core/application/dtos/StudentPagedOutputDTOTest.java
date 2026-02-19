package start.students.core.application.dtos;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DisplayName("StudentPagedOutputDTO Tests")
class StudentPagedOutputDTOTest {

    @Test
    @DisplayName("Should build StudentPagedOutputDTO successfully")
    void shouldBuildStudentPagedOutputDTOSuccessfully() {
        // ARRANGE
        StudentOutputDTO student1 = StudentOutputDTO.builder().id("uuid-1").name("João Silva").build();
        StudentOutputDTO student2 = StudentOutputDTO.builder().id("uuid-2").name("Maria Santos").build();
        List<StudentOutputDTO> content = Arrays.asList(student1, student2);

        // ACT
        StudentPagedOutputDTO dto = StudentPagedOutputDTO.builder()
                .content(content)
                .page(0)
                .size(10)
                .totalElements(2)
                .totalPages(1)
                .first(true)
                .last(true)
                .build();

        // ASSERT
        assertThat(dto).isNotNull();
        assertThat(dto.getContent()).hasSize(2);
        assertThat(dto.getPage()).isEqualTo(0);
        assertThat(dto.getSize()).isEqualTo(10);
        assertThat(dto.getTotalElements()).isEqualTo(2);
        assertThat(dto.getTotalPages()).isEqualTo(1);
        assertThat(dto.isFirst()).isTrue();
        assertThat(dto.isLast()).isTrue();
    }

    @Test
    @DisplayName("Should build StudentPagedOutputDTO with multiple pages")
    void shouldBuildStudentPagedOutputDTOWithMultiplePages() {
        // ARRANGE
        StudentOutputDTO student = StudentOutputDTO.builder().id("uuid-1").name("João Silva").build();
        List<StudentOutputDTO> content = Arrays.asList(student);

        // ACT
        StudentPagedOutputDTO dto = StudentPagedOutputDTO.builder()
                .content(content)
                .page(0)
                .size(10)
                .totalElements(20)
                .totalPages(2)
                .first(true)
                .last(false)
                .build();

        // ASSERT
        assertThat(dto).isNotNull();
        assertThat(dto.getTotalPages()).isEqualTo(2);
        assertThat(dto.isFirst()).isTrue();
        assertThat(dto.isLast()).isFalse();
    }

    @Test
    @DisplayName("Should build StudentPagedOutputDTO for middle page")
    void shouldBuildStudentPagedOutputDTOForMiddlePage() {
        // ARRANGE
        StudentOutputDTO student = StudentOutputDTO.builder().id("uuid-1").name("João Silva").build();
        List<StudentOutputDTO> content = Arrays.asList(student);

        // ACT
        StudentPagedOutputDTO dto = StudentPagedOutputDTO.builder()
                .content(content)
                .page(1)
                .size(10)
                .totalElements(30)
                .totalPages(3)
                .first(false)
                .last(false)
                .build();

        // ASSERT
        assertThat(dto).isNotNull();
        assertThat(dto.getPage()).isEqualTo(1);
        assertThat(dto.isFirst()).isFalse();
        assertThat(dto.isLast()).isFalse();
    }
}

