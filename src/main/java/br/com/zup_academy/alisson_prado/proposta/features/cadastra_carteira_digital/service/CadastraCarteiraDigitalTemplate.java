package br.com.zup_academy.alisson_prado.proposta.features.cadastra_carteira_digital.service;

import br.com.zup_academy.alisson_prado.proposta.model.NomeCarteiraDigital;

public class CadastraCarteiraDigitalTemplate {
    private String email;
    private NomeCarteiraDigital carteira;

    public CadastraCarteiraDigitalTemplate(String email, NomeCarteiraDigital carteira) {
        this.email = email;
        this.carteira = carteira;
    }

    public String getEmail() {
        return email;
    }

    public NomeCarteiraDigital getCarteira() {
        return carteira;
    }
}
