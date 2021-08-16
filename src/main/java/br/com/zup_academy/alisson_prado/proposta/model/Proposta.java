package br.com.zup_academy.alisson_prado.proposta.model;

import org.springframework.util.Assert;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Entity
public class Proposta {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false, unique = true)
    private String idUuid;

    @NotNull
    @ManyToOne(cascade = CascadeType.PERSIST)
    private Cliente cliente;

    @Deprecated
    private Proposta() {
    }

    /**
     *
     * @param cliente NotNull
     */
    public Proposta(@NotNull Cliente cliente) {
        Assert.notNull(cliente, "Cliente não pode ser nulo");
        this.cliente = cliente;
        this.idUuid = UUID.randomUUID().toString();
    }

    public Long getId() {
        return id;
    }

    public String getIdUuid() {
        return idUuid;
    }

    public Cliente getCliente() {
        return cliente;
    }
}
