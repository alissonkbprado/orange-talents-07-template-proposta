package br.com.zup_academy.alisson_prado.proposta.features.cadastra_proposta.service.analise;

import br.com.zup_academy.alisson_prado.proposta.model.StatusTransacaoPagamento;
import br.com.zup_academy.alisson_prado.proposta.validacao.EnumNamePattern;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class SolicitaAnaliseResponse {

    @NotBlank
    private String documento;
    @NotBlank
    private String nome;
    @NotNull @EnumNamePattern(regexp = "SEM_RESTRICAO|COM_RESTRICAO")
    private StatusTransacaoPagamento resultadoSolicitacao;
    @NotBlank
    private String idProposta;

    public SolicitaAnaliseResponse(String documento, String nome, StatusTransacaoPagamento resultadoSolicitacao, String idProposta) {
        this.documento = documento;
        this.nome = nome;
        this.resultadoSolicitacao = resultadoSolicitacao;
        this.idProposta = idProposta;
    }

    public String getDocumento() {
        return documento;
    }

    public String getNome() {
        return nome;
    }

    public StatusTransacaoPagamento getResultadoSolicitacao() {
        return resultadoSolicitacao;
    }

    public String getIdProposta() {
        return idProposta;
    }
}
