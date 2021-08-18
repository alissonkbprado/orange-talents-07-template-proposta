package br.com.zup_academy.alisson_prado.proposta.features.cadastra_proposta.service.cartao;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "novo-cartao", url = "${cartoes.host}")
public interface NovoCartaoClientFeign {

    @GetMapping("${cartoes.novo-cartao}")
    NovoCartaoResponse geraCartao(@RequestParam("idProposta") String idProposta);

}
