package br.com.zup_academy.alisson_prado.proposta.features.aviso_viagem.service;

import java.time.LocalDate;

public class AvisoViagemTemplate {

    private String destino;
    private LocalDate validoAte;

    public AvisoViagemTemplate(String destino, LocalDate validoAte) {
        this.destino = destino;
        this.validoAte = validoAte;
    }

    public String getDestino() {
        return destino;
    }

    public LocalDate getValidoAte() {
        return validoAte;
    }
}
