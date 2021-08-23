package br.com.zup_academy.alisson_prado.proposta.features.bloqueio_cartao.service;

import br.com.zup_academy.alisson_prado.proposta.features.novo_cartao.NovoCartaoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "bloqueia-cartao", url = "${cartoes.host}")
public interface BloqueiaCartaoClientFeign {

    @PostMapping("${cartoes.bloqueia-cartao}")
    NovoCartaoResponse bloqueiaCartao(@PathVariable(value = "id") String id, BloqueiaCartaoTemplate bloqueiaCartaoTemplate);

}
