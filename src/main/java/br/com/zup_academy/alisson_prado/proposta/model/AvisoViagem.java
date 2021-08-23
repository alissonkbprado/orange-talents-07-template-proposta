package br.com.zup_academy.alisson_prado.proposta.model;

import br.com.zup_academy.alisson_prado.proposta.validacao.Uuid;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.util.Assert;

import javax.persistence.*;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class AvisoViagem {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank @Uuid
    @Column(nullable = false, unique = true)
    private String idAvisoViagem;

    @NotBlank
    @Column(nullable = false)
    private String paisDestino;

    @NotNull @Future
    @Column(nullable = false)
    private LocalDate dataTermino;

    @CreationTimestamp
    @Column(nullable = false)
	private LocalDateTime dataCadastro;

    @NotBlank
    @Column(nullable = false)
    private String ipAddress;

    @NotBlank
    @Column(nullable = false)
	private String userAgent;

    @NotNull
    @ManyToOne(cascade = CascadeType.PERSIST)
	private Cartao cartao;

    private AvisoViagem() {
    }

    /**
     *
     * @param paisDestino NotBlank
     * @param dataTermino Future, NotNull
     * @param ipAddress ip da requisição, NotBlank
     * @param userAgent NotBlank
     * @param cartao NotNull
     */
    public AvisoViagem(@NotBlank String paisDestino,
                       @NotNull @Future LocalDate dataTermino,
                       @NotBlank String ipAddress,
                       @NotBlank String userAgent,
                       @NotNull Cartao cartao) {

        valida(paisDestino, dataTermino, ipAddress, userAgent, cartao);

        this.paisDestino = paisDestino.toString().toUpperCase();
        this.dataTermino = dataTermino;
        this.ipAddress = ipAddress;
        this.userAgent = userAgent;
        this.cartao = cartao;
        this.idAvisoViagem = UUID.randomUUID().toString();
    }

    private void valida(String paisDestino, LocalDate dataTermino, String ipAddress, String userAgent, Cartao cartao) {
        Assert.hasText(paisDestino, "Campo obrigatório");
        Assert.notNull(dataTermino, "Campo não pode ser nulo");
        Assert.hasText(ipAddress, "Campo obrigatório");
        Assert.hasText(userAgent, "Campo obrigatório");
        Assert.notNull(cartao, "Campo não pode ser nulo");
    }
}
