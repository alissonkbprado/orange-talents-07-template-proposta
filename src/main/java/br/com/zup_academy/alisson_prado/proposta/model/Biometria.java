package br.com.zup_academy.alisson_prado.proposta.model;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.util.Assert;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class Biometria {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String idBiometria;

    @NotBlank
    @Column(nullable = false)
    private String biometria;

    @CreationTimestamp
    private LocalDateTime dataCadastro;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private Cartao cartao;

    @Deprecated
    private Biometria() {
    }

    /**
     *
     * @param biometriaDecodificada Decodificada, NotBlank
     */
    public Biometria(@NotBlank String biometriaDecodificada, @NotNull Cartao cartao) {
        Assert.hasText(biometriaDecodificada, "Campo obrigatório");
        Assert.notNull(cartao, "Campo não pode ser nulo");

        this.biometria = biometriaDecodificada;
        this.cartao = cartao;
        this.idBiometria = UUID.randomUUID().toString();
    }

    public String getIdBiometria() {
        return idBiometria;
    }

    public String getBiometria() {
        return biometria;
    }
}
