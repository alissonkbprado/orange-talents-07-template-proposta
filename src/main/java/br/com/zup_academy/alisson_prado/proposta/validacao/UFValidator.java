package br.com.zup_academy.alisson_prado.proposta.validacao;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;


public class UFValidator implements ConstraintValidator<UF, String> {
    private List<String> listaUf;

    @Override
    public boolean isValid(String uf, ConstraintValidatorContext constraint) {

        listaUf = Arrays.asList("AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO", "MA",
                "MT", "MS", "MG", "PA", "PB", "PR", "PE", "PI", "RJ", "RN",
                "RS", "RO", "RR", "SC", "SP", "SE", "TO");

        return listaUf.contains(uf);

    }
}
