package br.com.zup_academy.alisson_prado.proposta.model;

import br.com.zup_academy.alisson_prado.proposta.validacao.UF;
import org.springframework.util.Assert;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Embeddable
public class Endereco {

    @NotBlank
    private String logradouro;

    @NotBlank
    private String numero;

    private String complemento;

    @NotBlank
    private String bairro;

    @NotBlank
    private String cidade;

    @NotBlank @Size(min = 2, max = 2) @UF
    private String uf;

    @NotBlank
    private String pais;

    @Deprecated
    private Endereco() {
    }

    /**
     *
     * @param logradouro NotBlank
     * @param numero NotBlank
     * @param complemento
     * @param bairro NotBlank
     * @param cidade NotBlank
     * @param uf NotBlank, Formato sigla UF
     * @param pais NotBlank
     */
    public Endereco(@NotBlank String logradouro,
                    @NotBlank String numero,
                    String complemento,
                    @NotBlank String bairro,
                    @NotBlank String cidade,
                    @NotBlank String uf,
                    @NotBlank String pais) {

        valida(logradouro, numero, bairro, cidade, uf, pais);

        this.logradouro = logradouro.trim().toUpperCase();
        this.numero = numero.trim().toUpperCase();
        if(complemento != null)
            this.complemento = complemento.trim().toUpperCase();
        this.bairro = bairro.trim().toUpperCase();
        this.cidade = cidade.trim().toUpperCase();
        this.uf = uf.trim().toUpperCase();
        this.pais = pais.trim().toUpperCase();
    }

    private void valida(String logradouro, String numero, String bairro, String cidade, String uf, String pais) {
        Assert.notNull(logradouro, "Campo obrigatório");
        Assert.hasText(logradouro, "Campo obrigatório");
        Assert.notNull(numero, "Campo obrigatório");
        Assert.hasText(numero, "Campo obrigatório");
        Assert.notNull(bairro, "Campo obrigatório");
        Assert.hasText(bairro, "Campo obrigatório");
        Assert.notNull(cidade, "Campo obrigatório");
        Assert.hasText(cidade, "Campo obrigatório");
        Assert.notNull(uf, "Campo obrigatório");
        Assert.hasText(uf, "Campo obrigatório");
        Assert.notNull(pais, "Campo obrigatório");
        Assert.hasText(pais, "Campo obrigatório");
    }

    public String getLogradouro() {
        return logradouro;
    }

    public String getNumero() {
        return numero;
    }

    public String getComplemento() {
        return complemento;
    }

    public String getBairro() {
        return bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public String getUf() {
        return uf;
    }

    public String getPais() {
        return pais;
    }
}
