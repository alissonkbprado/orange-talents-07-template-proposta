package br.com.zup_academy.alisson_prado.proposta.features.aviso_viagem.service;

import br.com.zup_academy.alisson_prado.proposta.model.Proposta;
import feign.FeignException;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;


@Component
public class AvisoViagemService {

    private AvisoViagemClientFeign clientFeign;
    private final MeterRegistry meterRegistry;
    private final Logger logger = LoggerFactory.getLogger(Proposta.class);

    public AvisoViagemService(AvisoViagemClientFeign clientFeign, MeterRegistry meterRegistry) {
        this.clientFeign = clientFeign;
        this.meterRegistry = meterRegistry;
    }

    public boolean enviaAvisoApi(String numeroCartao, String destino, LocalDate dataTermino){
        try{
            AvisoViagemTemplate avisoViagemTemplate = new AvisoViagemTemplate(destino, dataTermino);

            AvisoViagemResponse response = clientFeign.avisoViagemCartao(numeroCartao, avisoViagemTemplate);

            // Se não lançar Exception retorna Status 200 e foi gravado o aviso viagem na api de cartões
            return true;

        } catch (FeignException e){
            // Houve falha com a API
            logger.error("Não foi possível enviar o Aviso Viagem devido a falha de comunicação com a API de cartões.: " + e.getMessage());

            // Incrementa um contador quando houver erro com a api de cartões
            metricaContadorFalhaAvisoViagem();
            return false;
        }
    }

    private void metricaContadorFalhaAvisoViagem() {
        Collection<Tag> tags = new ArrayList<>();
        tags.add(Tag.of("emissora", "Mastercard"));
        tags.add(Tag.of("banco", "Itaú"));

        Counter contadorDeFalhaAvisoViagem = this.meterRegistry.counter("aviso_viagem_erro", tags);

        contadorDeFalhaAvisoViagem.increment();
    }


}
