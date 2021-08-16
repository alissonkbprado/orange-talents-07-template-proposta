package br.com.zup_academy.alisson_prado.proposta.features.cadastra_proposta.response;

import br.com.zup_academy.alisson_prado.proposta.model.Proposta;

import java.math.BigDecimal;

public class CadastraPropostaResponse {

    private String idUuid;
    private String nome;
    private String email;
    private BigDecimal salarioBruto;
    private EnderecoResponse endereco;

    public CadastraPropostaResponse(Proposta proposta) {
        this.idUuid = proposta.getIdUuid();
        this.nome = proposta.getCliente().getNome();
        this.email = proposta.getCliente().getEmail();
        this.salarioBruto = proposta.getCliente().getSalarioBruto();
        this.endereco = new EnderecoResponse(proposta.getCliente().getEndereco());
    }

    public String getIdUuid() {
        return idUuid;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public BigDecimal getSalarioBruto() {
        return salarioBruto;
    }

    public EnderecoResponse getEndereco() {
        return endereco;
    }
}
