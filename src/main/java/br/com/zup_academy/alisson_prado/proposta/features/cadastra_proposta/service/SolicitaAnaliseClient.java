package br.com.zup_academy.alisson_prado.proposta.features.cadastra_proposta.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "solicita-analise", url = "${cartoes.host}")
public interface SolicitaAnaliseClient {

    @PostMapping("${cartoes.host-analise}")
    SolicitaAnaliseRequest solicitaAnalise(SolicitaAnaliseTemplate template);
}
