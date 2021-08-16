package br.com.zup_academy.alisson_prado.proposta.validacao;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = UFValidator.class)
@Documented
@Target(FIELD)
@Retention(RUNTIME)
public @interface UF {
    String message() default ("O campo Estado deve ser uma sigla válida.");

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
