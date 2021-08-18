package br.com.zup_academy.alisson_prado.proposta.model;

import br.com.zup_academy.alisson_prado.proposta.validacao.CpfOrCnpj;
import org.springframework.util.Assert;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Cliente {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String nome;

    @NotBlank @Email
    @Column(nullable = false)
    private String email;

    @CpfOrCnpj
    @NotBlank
    @Column(nullable = false, unique = true)
    private String documento;

    @NotNull
    @PositiveOrZero
    @Column(nullable = false)
    private BigDecimal salarioBruto;

    @NotNull
    @Embedded
    private Endereco endereco;

    @OneToMany(mappedBy = "cliente")
    private List<Proposta> propostaList = new ArrayList<>();

    @OneToMany(mappedBy = "cliente")
    private List<Cartao> cartaoList = new ArrayList<>();

    @Deprecated
    private Cliente() {
    }

    /**
     *
     * @param nome NotBlank
     * @param email NotBlank
     * @param documento Unique, CPF or CNPJ, NotBlank
     * @param salarioBruto NotNull, Positive
     * @param endereco NotNull
     */
    public Cliente(@NotBlank String nome,
                   @NotBlank String email,
                   @NotNull @CpfOrCnpj String documento,
                   @NotNull BigDecimal salarioBruto,
                   @NotNull Endereco endereco) {

        valida(nome, email, documento, salarioBruto, endereco);

        this.nome = nome.trim().toUpperCase();
        this.email = email;
        this.documento = documento.toString().replaceAll("\\p{Punct}", "");
        this.salarioBruto = salarioBruto;
        this.endereco = endereco;
    }

    private void valida(String nome, String email, String documento, BigDecimal salarioBruto, Endereco endereco) {
        Assert.notNull(nome, "Campo obrigatório");
        Assert.hasText(nome, "Campo obrigatório");
        Assert.notNull(email, "Campo obrigatório");
        Assert.hasText(email, "Campo obrigatório");
        Assert.notNull(documento, "Campo obrigatório");
        Assert.hasText(documento, "Campo obrigatório");
        Assert.notNull(salarioBruto, "Campo obrigatório");
        Assert.notNull(endereco, "Campo obrigatório");
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public String getDocumento() {
        return documento;
    }

    public BigDecimal getSalarioBruto() {
        return salarioBruto;
    }
}
