package start.students.core.domain.valueobjects;

import lombok.Value;
import start.students.core.domain.exceptions.DomainException;

@Value
public class Email {
    String value;

    public Email(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new DomainException("Email não pode ser vazio");
        }

        if (!isValidEmail(value.trim())) {
            throw new DomainException("Email inválido");
        }

        this.value = value.trim().toLowerCase();
    }

    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$");
    }
}
