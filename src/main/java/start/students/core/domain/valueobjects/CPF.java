package start.students.core.domain.valueobjects;

import lombok.Value;
import start.students.core.domain.exceptions.DomainException;

@Value
public class CPF {
    String value;

    public CPF(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new DomainException("CPF não pode ser vazio");
        }

        String cleanCpf = value.replaceAll("[^0-9]", "");

        if (cleanCpf.length() != 11) {
            throw new DomainException("CPF deve ter 11 dígitos");
        }

        if (!isValidCpf(cleanCpf)) {
            throw new DomainException("CPF inválido");
        }

        this.value = cleanCpf;
    }

    private boolean isValidCpf(String cpf) {
        // Verificar se todos os dígitos são iguais
        if (cpf.chars().distinct().count() == 1) {
            return false;
        }

        // Validar dígito verificador
        int[] digits = cpf.chars().map(c -> c - '0').toArray();

        int sum1 = 0;
        for (int i = 0; i < 9; i++) {
            sum1 += digits[i] * (10 - i);
        }
        int digit1 = 11 - (sum1 % 11);
        if (digit1 > 9) digit1 = 0;

        int sum2 = 0;
        for (int i = 0; i < 10; i++) {
            sum2 += digits[i] * (11 - i);
        }
        int digit2 = 11 - (sum2 % 11);
        if (digit2 > 9) digit2 = 0;

        return digits[9] == digit1 && digits[10] == digit2;
    }
}
