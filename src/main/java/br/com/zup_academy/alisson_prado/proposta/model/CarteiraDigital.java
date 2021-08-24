package br.com.zup_academy.alisson_prado.proposta.model;

import br.com.zup_academy.alisson_prado.proposta.validacao.Uuid;
import org.springframework.util.Assert;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Entity
public class CarteiraDigital {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank @Uuid
    @Column(nullable = false, unique = true)
    private String idCarteiraDigital;

    @NotBlank
    @Column(nullable = false)
	private String idApiCartoes;

    @NotNull @Enumerated(EnumType.STRING)
    @Column(nullable = false)
	private NomeCarteiraDigital nome;

    @NotBlank @Email
    @Column(nullable = false)
    private String email;

    @NotNull
    @Column(nullable = false) @Enumerated(EnumType.STRING)
	private StatusCarteiraDigital status;

    @ManyToOne
	private Cartao cartao;

    private CarteiraDigital() {
    }

    /**
     *
     * @param nome NotNull, ENUM
     * @param email NotBlank, Email
     * @param cartao NotNull
     */
    public CarteiraDigital(@NotNull NomeCarteiraDigital nome,
                           @NotBlank @Email String email,
                           @NotNull Cartao cartao) {

        Assert.notNull(nome, "Campo não pode ser nulo");
        Assert.hasText(email, "Campo obrigatório");
        Assert.notNull(cartao, "Campo não pode ser nulo");

        this.nome = nome;
        this.email = email;
        this.cartao = cartao;
        this.idCarteiraDigital = UUID.randomUUID().toString();
        this.status = StatusCarteiraDigital.ASSOCIADA;
    }

    /**
     * Utilizar apenas no Service CadastraCarteiraDigitalService
     * @param idApiCartoes Uuid Format, NotBlank (Recebida pela API de cartões)
     *
     */
    public void setIdApiCartoes(@NotBlank String idApiCartoes) {
        Assert.hasText(idApiCartoes, "Campo obrigatório");
        this.idApiCartoes = idApiCartoes;
    }

    public String getIdCarteiraDigital() {
        return idCarteiraDigital;
    }

    public String getEmail() {
        return email;
    }

    public NomeCarteiraDigital getNome() {
        return nome;
    }

    public Cartao getCartao() {
        return cartao;
    }
}
