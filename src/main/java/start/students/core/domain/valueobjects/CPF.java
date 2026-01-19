package start.students.core.domain.valueobjects;

public class CPF {

    private final String value;

    public CPF(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("CPF não pode ser vazio");
        }
        String cleanCpf = value.replaceAll("\\D", "");

        if (cleanCpf.length() != 11) {
            throw new IllegalArgumentException("CPF deve ter 11 dígitos");
        }

        if (!isValidCPF(cleanCpf)) {
            throw new IllegalArgumentException("CPF inválido: " + value);
        }

        this.value = cleanCpf;
    }

    private boolean isValidCPF(String cpf) {
        if (cpf.matches("(\\d)\\1{10}")) return false;

        int sum = 0;
        int remainder;

        for (int i = 1; i <= 9; i++) {
            sum += Integer.parseInt(cpf.substring(i - 1, i)) * (11 - i);
        }

        remainder = (sum * 10) % 11;
        if (remainder == 10 || remainder == 11) remainder = 0;
        if (remainder != Integer.parseInt(cpf.substring(9, 10))) return false;

        sum = 0;
        for (int i = 1; i <= 10; i++) {
            sum += Integer.parseInt(cpf.substring(i - 1, i)) * (12 - i);
        }

        remainder = (sum * 10) % 11;
        if (remainder == 10 || remainder == 11) remainder = 0;
        return remainder == Integer.parseInt(cpf.substring(10, 11));
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CPF cpf = (CPF) o;
        return value.equals(cpf.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
