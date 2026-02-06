package start.students.core.application.dtos.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PhoneValidator.class)
@Documented
public @interface ValidPhone {
    String message() default "Telefone inválido. Deve conter entre 8 e 11 dígitos e não pode ser composto apenas por zeros";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
