package br.com.zup_academy.alisson_prado.proposta.features.aviso_viagem;

import br.com.zup_academy.alisson_prado.proposta.exception.ApiErroException;
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

    public AvisoViagemController(CartaoRepository cartaoRepository, AvisoViagemRepository avisoViagemRepository) {
        this.cartaoRepository = cartaoRepository;
        this.avisoViagemRepository = avisoViagemRepository;
    }

    @PostMapping("/api/v1/cartao/{idCartao}/aviso_viagem")
    public ResponseEntity<?> cadastra(@PathVariable @NotBlank String idCartao, @RequestBody @Valid AvisoViagemRequest request, HttpServletRequest httpServletRequest){

        if(idCartao == null || idCartao.isBlank() || isUuidNotValid(idCartao))
            throw new ApiErroException(HttpStatus.BAD_REQUEST, "Identificador do cartão não foi enviado ou é inválido.");

        Optional<Cartao> optionalCartao = cartaoRepository.findByIdCartao(idCartao);

        if(optionalCartao.isEmpty())
            return ResponseEntity.status(404).build();

        Cartao cartao = optionalCartao.get();

        AvisoViagem avisoViagem = request.toModel(cartao, httpServletRequest, avisoViagemRepository);

        avisoViagemRepository.save(avisoViagem);

        return ResponseEntity.ok().body("Aviso viagem cadastrado com sucesso.");
    }
}
