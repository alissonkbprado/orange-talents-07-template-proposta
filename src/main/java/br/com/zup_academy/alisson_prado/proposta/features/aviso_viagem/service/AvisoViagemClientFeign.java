package br.com.zup_academy.alisson_prado.proposta.features.aviso_viagem.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "aviso-viagem-cartao", url = "${cartoes.host}")
public interface AvisoViagemClientFeign {

    @PostMapping("${cartoes.aviso_viagem-cartao}")
    AvisoViagemResponse avisoViagemCartao(@PathVariable(value = "id") String id, AvisoViagemTemplate avisoViagemTemplate);
}
