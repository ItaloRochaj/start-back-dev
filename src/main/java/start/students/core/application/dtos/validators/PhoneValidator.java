package start.students.core.application.dtos.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validador customizado para Telefone
 * Telefone fixo: 10 dígitos (DDD + 8 números)
 * Telefone celular: 11 dígitos (DDD + 9 números)
 */
public class PhoneValidator implements ConstraintValidator<ValidPhone, String> {

    @Override
    public void initialize(ValidPhone constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // Campo vazio é permitido (telefone é opcional)
        if (value == null || value.trim().isEmpty()) {
            return true;
        }

        // Remover caracteres não-numéricos
        String phoneDigits = value.replaceAll("\\D", "");

        // Deve ter 10 dígitos (fixo) ou 11 dígitos (celular)
        if ((phoneDigits.length() != 10 && phoneDigits.length() != 11)) {
            addConstraintViolation(context, "Telefone deve ter 10 dígitos (fixo) ou 11 dígitos (celular)");
            return false;
        }

        // Não pode ser composto apenas por zeros (0000000 ou 00000000 etc)
        if (phoneDigits.matches("0+")) {
            addConstraintViolation(context, "Telefone inválido");
            return false;
        }

        // Não pode ser composto apenas por um dígito repetido (1111111 ou 2222222 etc)
        if (phoneDigits.matches("(.)\\1{" + (phoneDigits.length() - 1) + "}")) {
            addConstraintViolation(context, "Telefone inválido");
            return false;
        }

        return true;
    }

    private void addConstraintViolation(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
                .addConstraintViolation();
    }
}
