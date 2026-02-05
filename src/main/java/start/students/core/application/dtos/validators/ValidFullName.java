package start.students.core.application.dtos.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * Anotação customizada para validar nome completo
 * Exige pelo menos 2 palavras separadas por espaço
 */
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FullNameValidator.class)
@Documented
public @interface ValidFullName {
    String message() default "Nome deve ser completo (pelo menos 2 palavras)";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
