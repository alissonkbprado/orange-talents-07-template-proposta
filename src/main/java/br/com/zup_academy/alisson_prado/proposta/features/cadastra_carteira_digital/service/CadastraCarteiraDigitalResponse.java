package br.com.zup_academy.alisson_prado.proposta.features.cadastra_carteira_digital.service;

import br.com.zup_academy.alisson_prado.proposta.model.StatusCarteiraDigital;

public class CadastraCarteiraDigitalResponse {

    private StatusCarteiraDigital resultado;
    private String id;

    public CadastraCarteiraDigitalResponse(StatusCarteiraDigital resultado, String id) {
        this.resultado = resultado;
        this.id = id;
    }

    public StatusCarteiraDigital getResultado() {
        return resultado;
    }

    public String getId() {
        return id;
    }
}
