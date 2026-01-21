package start.students.core.domain.valueobjects;

import lombok.Value;
import start.students.core.domain.exceptions.DomainException;

@Value
public class Status {
    String value;

    public static final String ACTIVE = "ACTIVE";
    public static final String INACTIVE = "INACTIVE";
    public static final String SUSPENDED = "SUSPENDED";

    public Status(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new DomainException("Status não pode ser vazio");
        }

        String upperValue = value.trim().toUpperCase();
        if (!isValidStatus(upperValue)) {
            throw new DomainException("Status inválido. Valores aceitos: ACTIVE, INACTIVE, SUSPENDED");
        }

        this.value = upperValue;
    }

    private boolean isValidStatus(String status) {
        return ACTIVE.equals(status) || INACTIVE.equals(status) || SUSPENDED.equals(status);
    }

    public static Status active() {
        return new Status(ACTIVE);
    }

    public static Status inactive() {
        return new Status(INACTIVE);
    }

    public static Status suspended() {
        return new Status(SUSPENDED);
    }
}
