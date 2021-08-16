package br.com.zup_academy.alisson_prado.proposta.features.cadastra_proposta.request;

import br.com.zup_academy.alisson_prado.proposta.model.Endereco;
import br.com.zup_academy.alisson_prado.proposta.validacao.UF;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class EnderecoRequest {

    @NotBlank
    private String logradouro;

    @NotBlank
    private String numero;

    private String complemento;

    @NotBlank
    private String bairro;

    @NotBlank
    private String cidade;

    @NotBlank @Size(min = 2, max = 2) @UF(message = "Teste")
    private String uf;

    @NotBlank
    private String pais;

    public EnderecoRequest(String logradouro, String numero, String complemento, String bairro, String cidade, String uf, String pais) {
        this.logradouro = logradouro;
        this.numero = numero;
        this.complemento = complemento;
        this.bairro = bairro;
        this.cidade = cidade;
        this.uf = uf;
        this.pais = pais;
    }

    public Endereco toModel() {
        return new Endereco(this.logradouro,
                this.numero,
                this.complemento,
                this.bairro,
                this.cidade,
                this.uf,
                this.pais);

    }
}
