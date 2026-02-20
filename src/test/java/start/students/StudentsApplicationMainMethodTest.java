package start.students;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;

import static org.assertj.core.api.Assertions.*;

@DisplayName("StudentsApplication Main Method Tests")
class StudentsApplicationMainTest {

    @Test
    @DisplayName("should have main method callable")
    void shouldHaveMainMethodCallable() {
        // Arrange & Act & Assert
        assertThatCode(() -> {
            Class<?> appClass = Class.forName("start.students.StudentsApplication");
            assertThat(appClass).isNotNull();

            var mainMethod = appClass.getDeclaredMethod("main", String[].class);
            assertThat(mainMethod).isNotNull();
            assertThat(mainMethod.getName()).isEqualTo("main");
        }).doesNotThrowAnyException();
    }

}

