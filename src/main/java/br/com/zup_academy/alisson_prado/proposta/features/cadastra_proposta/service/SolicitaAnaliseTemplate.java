package br.com.zup_academy.alisson_prado.proposta.features.cadastra_proposta.service;

public class SolicitaAnaliseTemplate {

    private String documento;
    private String nome;
    private String idProposta;

    public SolicitaAnaliseTemplate(String documento, String nome, String idProposta) {
        this.documento = documento;
        this.nome = nome;
        this.idProposta = idProposta;
    }

    public String getDocumento() {
        return documento;
    }

    public String getNome() {
        return nome;
    }

    public String getIdProposta() {
        return idProposta;
    }
}
