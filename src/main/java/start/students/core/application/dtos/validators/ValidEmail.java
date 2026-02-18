package start.students.core.application.dtos.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * Anotação customizada para validar Email
 * Valida:
 * - Formato válido de email
 * - Rejeita caracteres acentuados (á, é, í, ó, ú, ã, õ, etc.)
 * - Rejeita cedilha (ç)
 */
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EmailValidator.class)
@Documented
public @interface ValidEmail {
    String message() default "Email deve conter apenas caracteres ASCII válidos (sem acentos ou cedilha)";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
