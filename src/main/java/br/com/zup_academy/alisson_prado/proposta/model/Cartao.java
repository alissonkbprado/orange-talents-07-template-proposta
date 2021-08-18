package br.com.zup_academy.alisson_prado.proposta.model;

import org.springframework.util.Assert;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
public class Cartao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String numero;

    @NotNull
    private LocalDateTime dataEmissao;

    @ManyToOne
    private Cliente cliente;

    @Deprecated
    private Cartao() {
    }

    /**
     *  @param numero NotBlank
     * @param dataEmissao NotNull
     * @param cliente
     */
    public Cartao(String numero, LocalDateTime dataEmissao, Cliente cliente) {
        Assert.hasText(numero, "Campo obrigatório");
        Assert.notNull(dataEmissao, "Campo não pode ser nulo");
        Assert.notNull(cliente, "Campo não pode ser nulo");

        this.numero = numero;
        this.dataEmissao = dataEmissao;
        this.cliente = cliente;
    }
}
