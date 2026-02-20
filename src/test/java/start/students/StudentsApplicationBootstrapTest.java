package start.students;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("StudentsApplication Bootstrap Tests")
class StudentsApplicationBootstrapTest {

    @Test
    @DisplayName("should have main method")
    void shouldHaveMainMethod() {
        // Arrange & Act & Assert
        assertThatNoException()
                .isThrownBy(() -> {
                    Class<?> clazz = Class.forName("start.students.StudentsApplication");
                    assertThat(clazz).isNotNull();
                });
    }

}

