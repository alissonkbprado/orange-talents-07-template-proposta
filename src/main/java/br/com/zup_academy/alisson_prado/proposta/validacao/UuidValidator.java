package br.com.zup_academy.alisson_prado.proposta.validacao;

import br.com.zup_academy.alisson_prado.proposta.compartilhado.ValidaUuid;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UuidValidator implements ConstraintValidator<Uuid, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return ValidaUuid.isUuidValid(value);
    }
}
