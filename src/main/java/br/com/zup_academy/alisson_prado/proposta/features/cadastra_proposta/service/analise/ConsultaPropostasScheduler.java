package br.com.zup_academy.alisson_prado.proposta.features.cadastra_proposta.service.analise;

import br.com.zup_academy.alisson_prado.proposta.model.Proposta;
import br.com.zup_academy.alisson_prado.proposta.model.StatusProposta;
import br.com.zup_academy.alisson_prado.proposta.repository.PropostaRepository;
import io.opentracing.Span;
import io.opentracing.Tracer;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ConsultaPropostasScheduler {

    private PropostaRepository propostaRepository;
    private SolicitaAnaliseClientFeign client;
    private final Tracer tracer;

    public ConsultaPropostasScheduler(PropostaRepository propostaRepository, SolicitaAnaliseClientFeign client, Tracer tracer) {
        this.propostaRepository = propostaRepository;
        this.client = client;
        this.tracer = tracer;
    }

    /**
     * Caso ocorra problemas no momento do cadastro da proposta ao consultar a API externa de Analise,
     * a proposta é persistida com o Status (AGUARDANDO_APROVACAO.)
     *
     * Esta rotina acessa novamente a API Analise da Proposta e atualiza o Status.
     */
    @Scheduled(initialDelay = 10000, fixedDelayString = "${periodicidade.consultaPropostasAguardandoAprovacao}")
    protected void consultaPropostasAguardandoAprovacao(){
        List<Proposta> propostasAguardandoAprovacao = propostaRepository.findFirst100ByStatus(StatusProposta.AGUARDANDO_APROVACAO);

        propostasAguardandoAprovacao.forEach( proposta -> {
            SolicitaAnaliseTemplate analiseTemplate = new SolicitaAnaliseTemplate(proposta.getCliente().getDocumento(),
                    proposta.getCliente().getNome(),
                    proposta.getIdProposta());

            proposta.avaliaRestricoes(client, tracer);
            propostaRepository.save(proposta);

            setBaggage(proposta.getIdProposta(), proposta.getCliente().getEmail());
        });
    }

    // Baggage Tracing
    private void setBaggage(String idProposta, String email) {
        Span activeSpan = tracer.activeSpan();
        activeSpan.setBaggageItem("user.id", idProposta);
        activeSpan.setBaggageItem("user.email", email);
    }
}
