package br.com.zup_academy.alisson_prado.proposta.features.novo_cartao;

import br.com.zup_academy.alisson_prado.proposta.model.Cartao;
import br.com.zup_academy.alisson_prado.proposta.model.Proposta;
import br.com.zup_academy.alisson_prado.proposta.model.StatusProposta;
import br.com.zup_academy.alisson_prado.proposta.repository.CartaoRepository;
import br.com.zup_academy.alisson_prado.proposta.repository.PropostaRepository;
import io.opentracing.Span;
import io.opentracing.Tracer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GeraCartaoScheduler {

    private PropostaRepository propostaRepository;
    private CartaoRepository cartaoRepository;
    private NovoCartaoClientFeign clientFeign;
    private final Logger logger = LoggerFactory.getLogger(GeraCartaoScheduler.class);
    private final Tracer tracer;

    public GeraCartaoScheduler(PropostaRepository propostaRepository, CartaoRepository cartaoRepository, NovoCartaoClientFeign clientFeign, Tracer tracer) {
        this.propostaRepository = propostaRepository;
        this.cartaoRepository = cartaoRepository;
        this.clientFeign = clientFeign;
        this.tracer = tracer;
    }

    /**
     * Rotina que consulta API externa de Cartões para gerar o número de cartão
     * das propostas com Status ELEGIVEL.
     *
     * Dados do Cartão persistidos na entidade Cartao.
     * A proposta é atualizada para Status APROVADO.
     */
    @Scheduled(initialDelay = 15000, fixedDelayString = "${periodicidade.gera-cartao}")
    protected void geraCartao(){
        List<Proposta> propostas = propostaRepository.findFirst100ByStatus(StatusProposta.ELEGIVEL);

        if(!propostas.isEmpty())
            propostas.forEach(proposta -> {
                try {
                    NovoCartaoResponse response = clientFeign.geraCartao(proposta.getIdProposta());
                    Cartao cartao = response.toModel(propostaRepository);

                    if (cartao != null) {
                        cartaoRepository.save(cartao);
                        proposta.aprovaProposta();
                        propostaRepository.save(proposta);
                    }

                    setBaggage(proposta.getIdProposta(), proposta.getCliente().getEmail());
                } catch (Exception e){
                    //Não foi possível acessar a API de cartões.
                    logger.error("Não foi possível gerar o número do cartão devido a falha de comunicação com a API de cartões: {}", e.getMessage());

                    setLogTracing("Não foi possível gerar o número do cartão devido a falha de comunicação com a API de cartões");
                }
            });
    }

    // Baggage Tracing
    private void setBaggage(String idProposta, String email) {
        Span activeSpan = tracer.activeSpan();
        activeSpan.setBaggageItem("user.id", idProposta);
        activeSpan.setBaggageItem("user.email", email);
    }

    // Log Tracing
    private void setLogTracing(String mensagem) {
        Span activeSpan = tracer.activeSpan();
        activeSpan.log(mensagem);
    }
}
