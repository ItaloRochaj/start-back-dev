package start.students.core.application.dtos.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

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

        // Deve ter entre 8 e 11 dígitos
        if (phoneDigits.length() < 8 || phoneDigits.length() > 11) {
            return false;
        }

        // Não pode ser composto apenas por zeros (0000000 ou 00000000 etc)
        if (phoneDigits.matches("0+")) {
            return false;
        }

        // Não pode ser composto apenas por um dígito repetido (1111111 ou 2222222 etc)
        if (phoneDigits.matches("(.)\\1{" + (phoneDigits.length() - 1) + "}")) {
            return false;
        }

        return true;
    }
}
