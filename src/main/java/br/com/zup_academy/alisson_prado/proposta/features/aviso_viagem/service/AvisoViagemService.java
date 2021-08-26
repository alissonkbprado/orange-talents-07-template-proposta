package br.com.zup_academy.alisson_prado.proposta.features.aviso_viagem.service;

import br.com.zup_academy.alisson_prado.proposta.metricas.Metricas;
import br.com.zup_academy.alisson_prado.proposta.model.Proposta;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;


@Service
public class AvisoViagemService {

    private AvisoViagemClientFeign clientFeign;
    private final Logger logger = LoggerFactory.getLogger(Proposta.class);
    private Metricas metricas;

    public AvisoViagemService(AvisoViagemClientFeign clientFeign, Metricas metricas) {
        this.clientFeign = clientFeign;
        this.metricas = metricas;
    }

    public boolean enviaAvisoViagemApi(String numeroCartao, String destino, LocalDate dataTermino){
        try{
            AvisoViagemTemplate avisoViagemTemplate = new AvisoViagemTemplate(destino, dataTermino);

            AvisoViagemResponse response = clientFeign.avisoViagemCartao(numeroCartao, avisoViagemTemplate);

            // Se não lançar Exception retorna Status 200 e foi gravado o aviso viagem na api de cartões
            return true;

        } catch (FeignException e){
            // Houve falha com a API
            logger.error("Não foi possível enviar o Aviso Viagem devido a falha de comunicação com a API de cartões.: " + e.getMessage());

            // Incrementa um contador quando houver erro com a api de cartões
            metricas.incrementaFalhaAvisoViagem("Mastercard", "Itau");
            return false;
        }
    }


}
