package br.com.zup_academy.alisson_prado.proposta.validacao;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = UuidValidator.class)
@Documented
@Target(FIELD)
@Retention(RUNTIME)
public @interface Uuid {

    String message() default ("O campo está em formato inválido.");

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
