package br.com.zup_academy.alisson_prado.proposta.features.cadastra_proposta.request;

import br.com.zup_academy.alisson_prado.proposta.model.Cliente;
import br.com.zup_academy.alisson_prado.proposta.model.Endereco;
import br.com.zup_academy.alisson_prado.proposta.model.Proposta;
import br.com.zup_academy.alisson_prado.proposta.validacao.CpfOrCnpj;
import br.com.zup_academy.alisson_prado.proposta.validacao.UniqueValue;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.math.BigDecimal;

public class CadastraPropostaRequest {

    @NotBlank
    private String nome;

    @NotBlank @Email
    private String email;

    @CpfOrCnpj @NotBlank @UniqueValue(domainClass = Cliente.class, fieldName = "documento")
    private String documento;

    @NotNull @PositiveOrZero
    private BigDecimal salarioBruto;

    @Valid
    private EnderecoRequest endereco;

    public CadastraPropostaRequest(String nome, String email, String documento, BigDecimal salarioBruto, EnderecoRequest endereco) {
        this.nome = nome;
        this.email = email;
        this.documento = documento;
        this.salarioBruto = salarioBruto;
        this.endereco = endereco;
    }

    public Proposta toModel() {
        Endereco endereco = this.endereco.toModel();

        Cliente cliente = new Cliente(this.nome,
                this.email,
                this.documento,
                this.salarioBruto,
                endereco);

        return new Proposta(cliente);
    }
}
