package br.com.zup_academy.alisson_prado.proposta.model;

import br.com.zup_academy.alisson_prado.proposta.features.bloqueio_cartao.service.BloqueiaCartaoClientFeign;
import br.com.zup_academy.alisson_prado.proposta.features.bloqueio_cartao.service.BloqueiaCartaoTemplate;
import br.com.zup_academy.alisson_prado.proposta.validacao.Uuid;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    @Transient
    private final Logger logger = LoggerFactory.getLogger(Proposta.class);

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank @Uuid
    @Column(nullable = false, unique = true)
    private String idCartao;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String numero;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime dataEmissao;

    @ManyToOne
    private Cliente cliente;

    @NotNull @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusCartao status;

    @OneToMany(mappedBy = "cartao")
    private List<Biometria> biometriaList = new ArrayList<>();

    @OneToMany(mappedBy = "cartao")
    private List<Bloqueio> bloqueioList = new ArrayList<>();

    @OneToMany(mappedBy = "cartao")
    private List<AvisoViagem> avisoViagemList = new ArrayList<>();

    @OneToMany(mappedBy = "cartao")
    private List<CarteiraDigital> carteiraDigitalList = new ArrayList<>();

    @Deprecated
    private Cartao() {
    }

    public Cartao(Long id) {
        this.id = id;
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
        this.status = StatusCartao.ATIVO;
    }

    public boolean isBloqueado() {
        if(this.status.equals(StatusCartao.BLOQUEADO))
            return true;

        return false;
    }

    public boolean bloqueia(BloqueiaCartaoClientFeign bloqueiaCartaoClientFeign) {
        try{
            bloqueiaCartaoClientFeign.bloqueiaCartao(this.numero, new BloqueiaCartaoTemplate("Sistema Propostas"));
            this.status = StatusCartao.BLOQUEADO;
            return true;
        } catch (FeignException.FeignClientException.UnprocessableEntity e) {
            // Este retorno indica que o cartão já está bloquado
            this.status = StatusCartao.BLOQUEADO;
            return true;
        } catch (Exception e) {
            this.status = StatusCartao.AGUARDANDO_BLOQUEIO;
            logger.error("Não foi possível bloquear o cartão devido a falha de comunicação com a API de cartões: " + e.getMessage());
            return false;
        }
    }

    public Long getId() {
        return id;
    }

    public String getIdCartao() {
        return idCartao;
    }

    public String getNumero() {
        return numero;
    }
}
