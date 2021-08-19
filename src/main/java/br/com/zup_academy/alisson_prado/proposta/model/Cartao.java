package br.com.zup_academy.alisson_prado.proposta.model;

import org.springframework.util.Assert;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
public class Cartao {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String idCartao;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String numero;

    @NotNull
    private LocalDateTime dataEmissao;

    @ManyToOne
    private Cliente cliente;

    @OneToMany(mappedBy = "cartao")
    private List<Biometria> biometriaList;

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
        this.idCartao = UUID.randomUUID().toString();
    }
}
