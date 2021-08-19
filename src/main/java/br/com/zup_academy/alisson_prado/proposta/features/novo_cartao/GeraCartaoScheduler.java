package br.com.zup_academy.alisson_prado.proposta.features.novo_cartao;

import br.com.zup_academy.alisson_prado.proposta.model.Cartao;
import br.com.zup_academy.alisson_prado.proposta.model.Proposta;
import br.com.zup_academy.alisson_prado.proposta.model.StatusProposta;
import br.com.zup_academy.alisson_prado.proposta.repository.CartaoRepository;
import br.com.zup_academy.alisson_prado.proposta.repository.PropostaRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GeraCartaoScheduler {

    private PropostaRepository propostaRepository;
    private CartaoRepository cartaoRepository;
    private NovoCartaoClientFeign clientFeign;

    public GeraCartaoScheduler(PropostaRepository propostaRepository, CartaoRepository cartaoRepository, NovoCartaoClientFeign clientFeign) {
        this.propostaRepository = propostaRepository;
        this.cartaoRepository = cartaoRepository;
        this.clientFeign = clientFeign;
    }

    /**
     * Rotina que consulta API externa de Cartões para gerar o número de cartão
     * das propostas com Status ELEGIVEL.
     *
     * Dados do Cartão persistidos na entidade Cartao.
     * A proposta é atualizada para Status APROVADO.
     */
    @Scheduled(initialDelay = 10000, fixedDelayString = "${periodicidade.geraCartao}")
    private void geraCartao(){

        List<Proposta> propostas = propostaRepository.findByStatus(StatusProposta.ELEGIVEL);

        if(!propostas.isEmpty())
            propostas.forEach(proposta -> {
                try {
                    NovoCartaoResponse cartaoRequest = clientFeign.geraCartao(proposta.getIdProposta());
                    Cartao cartao = cartaoRequest.toModel(propostaRepository);

                    if (cartao != null) {
                        cartaoRepository.save(cartao);
                        proposta.aprovaProposta();
                        propostaRepository.save(proposta);
                    }



                } catch (Exception e){
                    //Não foi possível acessar a API de cartões.
                }
            });

    }
}
