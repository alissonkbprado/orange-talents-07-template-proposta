package br.com.zup_academy.alisson_prado.proposta.model;

import br.com.zup_academy.alisson_prado.proposta.validacao.Uuid;
import org.springframework.util.Assert;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
public class Cartao {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank @Uuid
    @Column(nullable = false, unique = true)
    private String idCartao;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String numero;

    @NotNull
    private LocalDateTime dataEmissao;

    @ManyToOne
    private Cliente cliente;

    @NotNull
    private boolean bloqueado;

    @OneToMany(mappedBy = "cartao")
    private List<Biometria> biometriaList = new ArrayList<>();

    @OneToMany(mappedBy = "cartao")
    private List<Bloqueio> bloqueioList = new ArrayList<>();

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
        this.bloqueado = false;
    }

    public boolean isBloqueado() {
        return bloqueado;
    }

    public void bloqueia() {
        this.bloqueado = true;
    }
}
