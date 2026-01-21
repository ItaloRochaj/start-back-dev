package start.students.core.domain.valueobjects;

import lombok.Value;
import start.students.core.domain.exceptions.DomainException;

import java.util.UUID;

@Value
public class StudentId {
    String value;

    public StudentId(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new DomainException("ID do estudante não pode ser vazio");
        }

        this.value = value.trim();
    }

    public static StudentId generate() {
        return new StudentId(UUID.randomUUID().toString());
    }

}