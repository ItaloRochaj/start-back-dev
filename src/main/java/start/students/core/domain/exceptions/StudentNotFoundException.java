package start.students.core.domain.exceptions;

public class StudentNotFoundException extends DomainException {
    public StudentNotFoundException(String message) {
        super(message);
    }
}
