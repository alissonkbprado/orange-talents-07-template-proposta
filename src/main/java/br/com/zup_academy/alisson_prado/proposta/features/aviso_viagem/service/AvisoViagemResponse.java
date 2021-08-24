package br.com.zup_academy.alisson_prado.proposta.features.aviso_viagem.service;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AvisoViagemResponse {

    private String resultado;

    @JsonCreator
    public AvisoViagemResponse(@JsonProperty("resultado") String resultado) {
        this.resultado = resultado;
    }

    public String getResultado() {
        return resultado;
    }
}
