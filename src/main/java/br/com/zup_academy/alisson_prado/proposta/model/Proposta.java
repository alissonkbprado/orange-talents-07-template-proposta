package br.com.zup_academy.alisson_prado.proposta.model;

import br.com.zup_academy.alisson_prado.proposta.features.cadastra_proposta.service.analise.SolicitaAnaliseClientFeign;
import br.com.zup_academy.alisson_prado.proposta.features.cadastra_proposta.service.analise.SolicitaAnaliseResponse;
import br.com.zup_academy.alisson_prado.proposta.features.cadastra_proposta.service.analise.SolicitaAnaliseTemplate;
import br.com.zup_academy.alisson_prado.proposta.repository.PropostaRepository;
import feign.FeignException;
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

    @NotNull
    @Column(nullable = false, unique = true)
    private String idUuid;

    @NotNull
    @ManyToOne(cascade = CascadeType.PERSIST)
    private Cliente cliente;

    @Enumerated(EnumType.STRING)
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
        this.idUuid = UUID.randomUUID().toString();
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

    public String getIdUuid() {
        return idUuid;
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
     * @return
     */
    public void avaliaRestricoes(SolicitaAnaliseClientFeign clientFeign) {
        try{
            SolicitaAnaliseTemplate analiseTemplate = new SolicitaAnaliseTemplate(this.cliente.getDocumento(),
                    this.cliente.getNome(),
                    this.idUuid);

            SolicitaAnaliseResponse response = clientFeign.solicitaAnalise(analiseTemplate);

            // Se não lançar Exception retorna Status 201 e aprovação da proposta
            this.status = response.getResultadoSolicitacao().getStatusTransacaoPagamento();

        } catch (FeignException.UnprocessableEntity unprocessableEntity){
            // Lança exception 422 caso não tenha sido aprovado
            this.status = StatusProposta.NAO_ELEGIVEL;
        } catch (FeignException e){
            // Qualquer outro código de erro significa que houve falha com a API. Os dados são persistidos com Status AGUARDANDO_APROVACAO
            logger.error("Não foi possível realizar a análise da proposta devido a falha de comunicação com a API de análise.: " + e.getMessage());
        }
    }

    public void aprovaProposta() {
        this.status = StatusProposta.APROVADO;
    }
}
