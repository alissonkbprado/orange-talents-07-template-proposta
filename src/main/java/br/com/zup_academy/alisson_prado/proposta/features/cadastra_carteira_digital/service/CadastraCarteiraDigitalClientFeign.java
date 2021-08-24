package br.com.zup_academy.alisson_prado.proposta.features.cadastra_carteira_digital.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "cartao-cadastra-carteira-digital", url = "${cartoes.host}")
public interface CadastraCarteiraDigitalClientFeign {

    @PostMapping("${cartoes.cadastra_carteira_digital-cartao}")
    CadastraCarteiraDigitalResponse cadastra(@PathVariable(value = "id") String id, CadastraCarteiraDigitalTemplate template);
}
