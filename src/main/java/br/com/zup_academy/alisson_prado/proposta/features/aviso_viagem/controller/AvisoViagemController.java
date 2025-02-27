package br.com.zup_academy.alisson_prado.proposta.features.aviso_viagem.controller;

import br.com.zup_academy.alisson_prado.proposta.exception.ApiErroException;
import br.com.zup_academy.alisson_prado.proposta.features.aviso_viagem.request.AvisoViagemRequest;
import br.com.zup_academy.alisson_prado.proposta.features.aviso_viagem.service.AvisoViagemService;
import br.com.zup_academy.alisson_prado.proposta.model.AvisoViagem;
import br.com.zup_academy.alisson_prado.proposta.model.Cartao;
import br.com.zup_academy.alisson_prado.proposta.repository.AvisoViagemRepository;
import br.com.zup_academy.alisson_prado.proposta.repository.CartaoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.Optional;

import static br.com.zup_academy.alisson_prado.proposta.compartilhado.ValidaUuid.isUuidNotValid;

@RestController
public class AvisoViagemController {

    private CartaoRepository cartaoRepository;
    private AvisoViagemRepository avisoViagemRepository;
    private AvisoViagemService avisoViagemService;

    public AvisoViagemController(CartaoRepository cartaoRepository, AvisoViagemRepository avisoViagemRepository, AvisoViagemService avisoViagemService) {
        this.cartaoRepository = cartaoRepository;
        this.avisoViagemRepository = avisoViagemRepository;
        this.avisoViagemService = avisoViagemService;
    }

    @PostMapping("/api/v1/cartao/{idCartao}/aviso_viagem")
    public ResponseEntity<?> cadastraAvisoViagen(@PathVariable @NotBlank String idCartao, @RequestBody @Valid AvisoViagemRequest request, HttpServletRequest httpServletRequest){

        if(idCartao == null || idCartao.isBlank() || isUuidNotValid(idCartao))
            throw new ApiErroException(HttpStatus.BAD_REQUEST, "Identificador do cartão não foi enviado ou é inválido.");

        Optional<Cartao> optionalCartao = cartaoRepository.findByIdCartao(idCartao);

        if(optionalCartao.isEmpty())
            return ResponseEntity.status(404).build();

        Cartao cartao = optionalCartao.get();

        AvisoViagem avisoViagem = request.toModel(cartao, httpServletRequest, avisoViagemRepository);

        if (avisoViagemService.enviaAvisoViagemApi(cartao.getNumero(), request.getPaisDestino(), request.getDataTermino())){
            avisoViagemRepository.save(avisoViagem);
            return ResponseEntity.ok().body("Aviso viagem cadastrado com sucesso.");
        }

        throw new ApiErroException(HttpStatus.SERVICE_UNAVAILABLE, "Não foi possível efetuar o cadastro de aviso viagem no momento devido a falha de comunicação " +
                "com a operadora de cartão de crédito. Por favor tente novamente mais tarde.");
    }
}
