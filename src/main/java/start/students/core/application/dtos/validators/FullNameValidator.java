package start.students.core.application.dtos.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Implementação do validador de nome completo
 */
public class FullNameValidator implements ConstraintValidator<ValidFullName, String> {

    @Override
    public void initialize(ValidFullName constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // Null é aceito (deixar required validator cuidar disso)
        if (value == null) {
            return true;
        }

        // Trim e validar
        String trimmedName = value.trim();
        
        // Dividir por espaços em branco
        String[] parts = trimmedName.split("\\s+");

        // Deve ter pelo menos 2 palavras
        if (parts.length < 2) {
            addConstraintViolation(context, "Nome deve conter pelo menos 2 palavras");
            return false;
        }

        // Cada palavra deve ter pelo menos 2 caracteres
        for (String part : parts) {
            if (part.length() < 2) {
                addConstraintViolation(context, "Cada palavra do nome deve ter pelo menos 2 caracteres");
                return false;
            }
        }

        return true;
    }

    private void addConstraintViolation(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
                .addConstraintViolation();
    }
}
