package br.com.zup_academy.alisson_prado.proposta.features.cadastra_carteira_digital.service;

import br.com.zup_academy.alisson_prado.proposta.model.CarteiraDigital;
import br.com.zup_academy.alisson_prado.proposta.model.StatusCarteiraDigital;
import feign.FeignException;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class CadastraCarteiraDigitalService {

    private CadastraCarteiraDigitalClientFeign clientFeign;
    private final MeterRegistry meterRegistry;
    private final Logger logger = LoggerFactory.getLogger(CadastraCarteiraDigitalService.class);

    public CadastraCarteiraDigitalService(CadastraCarteiraDigitalClientFeign clientFeign, MeterRegistry meterRegistry) {
        this.clientFeign = clientFeign;
        this.meterRegistry = meterRegistry;
    }

    public boolean enviaApi(CarteiraDigital carteiraDigital){
        try{
            CadastraCarteiraDigitalTemplate template = new CadastraCarteiraDigitalTemplate(carteiraDigital.getEmail(), carteiraDigital.getNome());

            CadastraCarteiraDigitalResponse response = clientFeign.cadastraCarteiraDigital(carteiraDigital.getCartao().getNumero(), template);

            // Se não lançar Exception retorna Status 200 e foi gravado o aviso viagem na api de cartões
            if (response.getResultado().equals(StatusCarteiraDigital.ASSOCIADA)){
                carteiraDigital.setIdApiCartoes(response.getId());
                return true;
            }

            return false;

        } catch (FeignException e){
            // Houve falha com a API
            logger.error("Não foi possível cadastrar a carteira digital devido a falha de comunicação com a API de cartões: {}", e.getMessage());

            // Incrementa um contador quando houver erro com a api de cartões
            metricaContadorFalhaCarteiraDigital();
            return false;
        }
    }

    private void metricaContadorFalhaCarteiraDigital() {
        Collection<Tag> tags = new ArrayList<>();
        tags.add(Tag.of("api", "cartoes"));

        Counter contadorDeFalhaCadastroCarteiraDigital = this.meterRegistry.counter("cadastra_carteira_digital_erro", tags);

        contadorDeFalhaCadastroCarteiraDigital.increment();
    }
}
