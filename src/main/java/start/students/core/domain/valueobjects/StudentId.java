package start.students.core.domain.valueobjects;

public class StudentId {
    private final String value;

    public StudentId() {
        this.value = java.util.UUID.randomUUID().toString();
    }

    public StudentId(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("StudentId não pode ser vazio");
        }
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StudentId studentId = (StudentId) o;
        return value.equals(studentId.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return value;
    }

}