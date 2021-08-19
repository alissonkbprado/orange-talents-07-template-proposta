package br.com.zup_academy.alisson_prado.proposta.features.cadastra_proposta.service.analise;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "solicita-analise", url = "${analise.host}")
public interface SolicitaAnaliseClientFeign {

    @PostMapping("${analise.solicitacao-analise}")
    SolicitaAnaliseResponse solicitaAnalise(SolicitaAnaliseTemplate template);
}
