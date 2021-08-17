package br.com.zup_academy.alisson_prado.proposta.validacao;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = EnumNamePatternValidator.class)
@Documented
@Target(FIELD)
@Retention(RUNTIME)
public @interface EnumNamePattern {
    String message() default ("Deve ser passado um dos seguintes valores \"{regexp}\"");

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String regexp();
}
