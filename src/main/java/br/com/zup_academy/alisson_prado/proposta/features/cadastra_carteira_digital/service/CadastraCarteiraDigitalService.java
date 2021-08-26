package br.com.zup_academy.alisson_prado.proposta.features.cadastra_carteira_digital.service;

import br.com.zup_academy.alisson_prado.proposta.metricas.Metricas;
import br.com.zup_academy.alisson_prado.proposta.model.CarteiraDigital;
import br.com.zup_academy.alisson_prado.proposta.model.StatusCarteiraDigital;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CadastraCarteiraDigitalService {

    private CadastraCarteiraDigitalClientFeign clientFeign;
    private Metricas metricas;
    private final Logger logger = LoggerFactory.getLogger(CadastraCarteiraDigitalService.class);

    public CadastraCarteiraDigitalService(CadastraCarteiraDigitalClientFeign clientFeign, Metricas metricas) {
        this.clientFeign = clientFeign;
        this.metricas = metricas;
    }

    public boolean enviaCadastroCarteiraDigitalApi(CarteiraDigital carteiraDigital){
        try{
            CadastraCarteiraDigitalTemplate template = new CadastraCarteiraDigitalTemplate(carteiraDigital.getEmail(), carteiraDigital.getNome());

            CadastraCarteiraDigitalResponse response = clientFeign.cadastraCarteiraDigital(carteiraDigital.getCartao().getNumero(), template);

            // Se não lançar Exception retorna Status 200 e foi gravada Carteira Digital na api de cartões
            if (response.getResultado().equals(StatusCarteiraDigital.ASSOCIADA)){
                carteiraDigital.setIdApiCartoes(response.getId());
                return true;
            }

            return false;

        } catch (FeignException.UnprocessableEntity e){
            // Retournou Status 422 -> Já está cadstrado na Api de cartões
            logger.error("Carteira digital ja está cadastrada na API de cartões, não deveria chegar aqui, já deveria estar cadastrado em nosso banco de dados.: {}", e.getMessage());

            // Incrementa um contador quando houver erro com a api de cartões
            metricas.incrementaFalhaCarteiraDigital(carteiraDigital.getNome().toString());

            return false;
        } catch (FeignException e){
            // Houve falha com a API
            logger.error("Não foi possível cadastrar a carteira digital devido a falha de comunicação com a API de cartões: {}", e.getMessage());

            // Incrementa um contador quando houver erro com a api de cartões
            metricas.incrementaFalhaCarteiraDigital(carteiraDigital.getNome().toString());

            return false;
        }

    }
}
