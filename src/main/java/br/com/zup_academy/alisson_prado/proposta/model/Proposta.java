package br.com.zup_academy.alisson_prado.proposta.model;

import br.com.zup_academy.alisson_prado.proposta.features.cadastra_proposta.request.CadastraPropostaRequest;
import br.com.zup_academy.alisson_prado.proposta.features.cadastra_proposta.service.analise.SolicitaAnaliseClientFeign;
import br.com.zup_academy.alisson_prado.proposta.features.cadastra_proposta.service.analise.SolicitaAnaliseResponse;
import br.com.zup_academy.alisson_prado.proposta.features.cadastra_proposta.service.analise.SolicitaAnaliseTemplate;
import br.com.zup_academy.alisson_prado.proposta.repository.PropostaRepository;
import br.com.zup_academy.alisson_prado.proposta.validacao.Uuid;
import feign.FeignException;
import io.opentracing.Span;
import io.opentracing.Tracer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Entity
public class Proposta {

    @Transient
    private final Logger logger = LoggerFactory.getLogger(Proposta.class);

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull @Uuid
    @Column(nullable = false, unique = true)
    private String idProposta;

    @NotNull
    @ManyToOne(cascade = CascadeType.PERSIST)
    private Cliente cliente;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusProposta status;

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
        this.idProposta = UUID.randomUUID().toString();
        this.status = StatusProposta.AGUARDANDO_APROVACAO;
    }

    /**
     * Verifica se já existe registro com o ddocumento informado.
     * @param propostaRepository NotNull
     * @return
     */
    public boolean isDocumentoCadastrado(PropostaRepository propostaRepository){
        Assert.notNull(propostaRepository, "PropostaRepository não pode ser nulo.");
        return propostaRepository.existsByClienteDocumento(cliente.getDocumento());
    }

    public Long getId() {
        return id;
    }

    public String getIdProposta() {
        return idProposta;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public StatusProposta getStatus() {
        return status;
    }

    /**
     * Faz uma requisição POST para API de cartões que verifica se a proposta é elegível
     * @param clientFeign SolicitaAnaliseClientFeign NotNull
     * @param tracer
     * @return
     */
    public void avaliaRestricoes(SolicitaAnaliseClientFeign clientFeign, Tracer tracer) {
        try{
            SolicitaAnaliseTemplate analiseTemplate = new SolicitaAnaliseTemplate(this.cliente.getDocumento(),
                    this.cliente.getNome(),
                    this.idProposta);

            SolicitaAnaliseResponse response = clientFeign.solicitaAnalise(analiseTemplate);

            // Se não lançar Exception retorna Status 201 e aprovação da proposta
            this.status = StatusProposta.ELEGIVEL;

            // Adiciona Baggage para o Tracing
            setBaggage(tracer);
        } catch (FeignException.UnprocessableEntity unprocessableEntity){
            // Lança exception 422 caso não tenha sido aprovado
            this.status = StatusProposta.NAO_ELEGIVEL;

            setBaggage(tracer);
        } catch (FeignException e){
            // Qualquer outro código de erro significa que houve falha com a API. Os dados são persistidos com Status AGUARDANDO_APROVACAO
            logger.error("Não foi possível realizar a análise da proposta devido a falha de comunicação com a API de análise.: {}", e.getMessage());
        }
    }

    public void aprovaProposta() {
        this.status = StatusProposta.APROVADO;
    }

    // Baggage Tracing
    private void setBaggage(Tracer tracer) {
        Span activeSpan = tracer.activeSpan();
        String userEmail = activeSpan.getBaggageItem("user.email");
        String userId = activeSpan.getBaggageItem("user.id");
        activeSpan.setBaggageItem("user.id", userId);
        activeSpan.setBaggageItem("user.email", userEmail);
    }
}
