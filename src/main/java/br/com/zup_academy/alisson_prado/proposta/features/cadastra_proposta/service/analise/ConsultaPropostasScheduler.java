package br.com.zup_academy.alisson_prado.proposta.features.cadastra_proposta.service.analise;

import br.com.zup_academy.alisson_prado.proposta.model.Proposta;
import br.com.zup_academy.alisson_prado.proposta.model.StatusProposta;
import br.com.zup_academy.alisson_prado.proposta.repository.PropostaRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ConsultaPropostasScheduler {

    private PropostaRepository propostaRepository;
    private SolicitaAnaliseClientFeign client;

    public ConsultaPropostasScheduler(PropostaRepository propostaRepository, SolicitaAnaliseClientFeign client) {
        this.propostaRepository = propostaRepository;
        this.client = client;
    }

    /**
     * Caso ocorra problemas no momento do cadastro da proposta ao consultar a API externa de Analise,
     * a proposta é persistida com o Status (AGUARDANDO_APROVACAO.)
     *
     * Esta rotina acessa novamente a API Analise da Proposta e atualiza o Status.
     */
    @Scheduled(initialDelay = 10000, fixedDelayString = "${periodicidade.consultaPropostasAguardandoAprovacao}")
    private void consultaPropostasAguardandoAprovacao(){

        List<Proposta> propostasAguardandoAprovacao = propostaRepository.findByStatus(StatusProposta.AGUARDANDO_APROVACAO);

        propostasAguardandoAprovacao.forEach( proposta -> {
            SolicitaAnaliseTemplate analiseTemplate = new SolicitaAnaliseTemplate(proposta.getCliente().getDocumento(),
                    proposta.getCliente().getNome(),
                    proposta.getIdProposta());

            System.out.println("Status Proposta: " + proposta.getId() + " - " + proposta.getStatus());
            proposta.avaliaRestricoes(client);
            propostaRepository.save(proposta);

            System.out.println("Status Proposta depois da avaliação: " + proposta.getId() + " - " + proposta.getStatus());

        });
    }
}
