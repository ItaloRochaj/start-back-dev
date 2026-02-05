package start.students.core.application.dtos.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Implementação do validador de CPF
 * Valida o algoritmo de dígitos verificadores do CPF
 */
public class CPFValidator implements ConstraintValidator<ValidCPF, String> {

    @Override
    public void initialize(ValidCPF constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // Null é aceito (deixar required validator cuidar disso)
        if (value == null || value.trim().isEmpty()) {
            return true;
        }

        // Remover formatação
        String cpf = value.replaceAll("[^0-9]", "");

        // Validar comprimento
        if (cpf.length() != 11) {
            return false;
        }

        // Validar se não é sequência repetida (000.000.000-00, 111.111.111-11, etc)
        if (isRepeatedSequence(cpf)) {
            addConstraintViolation(context, "CPF inválido");
            return false;
        }

        // Validar dígitos verificadores
        if (!isValidCPF(cpf)) {
            addConstraintViolation(context, "CPF inválido");
            return false;
        }

        return true;
    }

    /**
     * Verifica se o CPF é uma sequência repetida (ex: 000.000.000-00)
     */
    private boolean isRepeatedSequence(String cpf) {
        return cpf.matches("(\\d)\\1{10}");
    }

    /**
     * Valida o CPF usando o algoritmo de dígitos verificadores
     */
    private boolean isValidCPF(String cpf) {
        // Extrair dígitos verificadores
        int digit1 = Integer.parseInt(String.valueOf(cpf.charAt(9)));
        int digit2 = Integer.parseInt(String.valueOf(cpf.charAt(10)));

        // Calcular primeiro dígito verificador
        int calculated1 = calculateFirstDigit(cpf);
        if (calculated1 != digit1) {
            return false;
        }

        // Calcular segundo dígito verificador
        int calculated2 = calculateSecondDigit(cpf);
        if (calculated2 != digit2) {
            return false;
        }

        return true;
    }

    /**
     * Calcula o primeiro dígito verificador
     * Multiplica cada um dos 9 primeiros dígitos por 10, 9, 8, ..., 2
     */
    private int calculateFirstDigit(String cpf) {
        int sum = 0;
        int multiplier = 10;

        for (int i = 0; i < 9; i++) {
            int digit = Integer.parseInt(String.valueOf(cpf.charAt(i)));
            sum += digit * multiplier;
            multiplier--;
        }

        int remainder = sum % 11;
        return remainder < 2 ? 0 : 11 - remainder;
    }

    /**
     * Calcula o segundo dígito verificador
     * Multiplica cada um dos 10 primeiros dígitos por 11, 10, 9, ..., 2
     */
    private int calculateSecondDigit(String cpf) {
        int sum = 0;
        int multiplier = 11;

        for (int i = 0; i < 10; i++) {
            int digit = Integer.parseInt(String.valueOf(cpf.charAt(i)));
            sum += digit * multiplier;
            multiplier--;
        }

        int remainder = sum % 11;
        return remainder < 2 ? 0 : 11 - remainder;
    }

    private void addConstraintViolation(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
                .addConstraintViolation();
    }
}
