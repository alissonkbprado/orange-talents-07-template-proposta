package br.com.zup_academy.alisson_prado.proposta.features.cadastra_proposta.response;

import br.com.zup_academy.alisson_prado.proposta.model.Proposta;
import br.com.zup_academy.alisson_prado.proposta.model.StatusProposta;

public class CadastraPropostaResponse {

    private String idProposta;
    private String nome;
    private String email;
    private StatusProposta status;

    public CadastraPropostaResponse(Proposta proposta) {
        this.idProposta = proposta.getIdUuid();
        this.nome = proposta.getCliente().getNome();
        this.email = proposta.getCliente().getEmail();
        this.status = proposta.getStatus();
    }

    public String getIdProposta() {
        return idProposta;
    }

    public StatusProposta getStatus() {
        return status;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }
}
