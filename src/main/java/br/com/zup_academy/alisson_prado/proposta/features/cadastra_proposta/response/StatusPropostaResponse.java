package br.com.zup_academy.alisson_prado.proposta.features.cadastra_proposta.response;

import br.com.zup_academy.alisson_prado.proposta.model.Proposta;
import br.com.zup_academy.alisson_prado.proposta.model.StatusProposta;

public class StatusPropostaResponse {

    private String idProposta;
    private String nome;
    private StatusProposta status;

    public StatusPropostaResponse(Proposta proposta) {
        this.idProposta = proposta.getIdProposta();
        this.nome = proposta.getCliente().getNome();
        this.status = proposta.getStatus();
    }

    public String getIdProposta() {
        return idProposta;
    }

    public String getNome() {
        return nome;
    }

    public StatusProposta getStatus() {
        return status;
    }
}
