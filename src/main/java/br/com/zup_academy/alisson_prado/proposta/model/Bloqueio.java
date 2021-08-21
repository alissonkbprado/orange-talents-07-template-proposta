package br.com.zup_academy.alisson_prado.proposta.model;

import br.com.zup_academy.alisson_prado.proposta.validacao.Uuid;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.util.Assert;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class Bloqueio {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank @Uuid
    private String idBloqueio;

    @CreationTimestamp
    private LocalDateTime data;

    @NotBlank
    private String ipAddress;

    @NotBlank
    private String userAgent;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private Cartao cartao;

    private Bloqueio() {
    }

    /**
     *
     * @param ipAddress NotBlank
     * @param userAgent NotBlank
     * @param cartao NoNull
     */
    public Bloqueio(@NotBlank String ipAddress, @NotBlank String userAgent, @NotNull Cartao cartao) {
        Assert.hasText(ipAddress, "Campo obrigatório");
        Assert.hasText(userAgent, "Campo obrigatório");
        Assert.notNull(cartao, "Campo não pode ser nulo");

        this.ipAddress = ipAddress;
        this.userAgent = userAgent;
        this.cartao = cartao;
        this.idBloqueio = UUID.randomUUID().toString();
    }

    public String getIdBloqueio() {
        return idBloqueio;
    }
}
