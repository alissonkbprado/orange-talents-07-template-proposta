package br.com.zup_academy.alisson_prado.proposta.features.bloqueio_cartao;

import br.com.zup_academy.alisson_prado.proposta.exception.ApiErroException;
import br.com.zup_academy.alisson_prado.proposta.model.Bloqueio;
import br.com.zup_academy.alisson_prado.proposta.model.Cartao;
import br.com.zup_academy.alisson_prado.proposta.repository.BloqueiaCartaoRepository;
import br.com.zup_academy.alisson_prado.proposta.repository.CartaoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.validation.constraints.NotBlank;
import java.util.Optional;

import static br.com.zup_academy.alisson_prado.proposta.compartilhado.GetIPAdress.getClientIp;
import static br.com.zup_academy.alisson_prado.proposta.compartilhado.ValidaUuid.isUuidNotValid;

@RestController
public class BloqueiaCartaoController {

    CartaoRepository cartaoRepository;
    BloqueiaCartaoRepository bloqueiaCartaoRepository;

    public BloqueiaCartaoController(CartaoRepository cartaoRepository, BloqueiaCartaoRepository bloqueiaCartaoRepository) {
        this.cartaoRepository = cartaoRepository;
        this.bloqueiaCartaoRepository = bloqueiaCartaoRepository;
    }

    @PostMapping("/api/v1/cartao/{idCartao}/bloqueia")
    @Transactional
    public ResponseEntity<?> bloqueia(@PathVariable @NotBlank String idCartao, HttpServletRequest request){

        if(idCartao == null || idCartao.isBlank() || isUuidNotValid(idCartao))
            throw new ApiErroException(HttpStatus.BAD_REQUEST, "Identificador do cartão não foi enviado ou é inválido.");

        Optional<Cartao> optionalCartao = cartaoRepository.findByIdCartao(idCartao);

        if(optionalCartao.isEmpty())
            return ResponseEntity.status(404).build();

        Cartao cartao = optionalCartao.get();

        if (cartao.isBloqueado())
            throw new ApiErroException(HttpStatus.UNPROCESSABLE_ENTITY, "O cartão já está bloqueado.");

        cartao.bloqueia();

        Bloqueio bloqueio = new Bloqueio(getClientIp(request), request.getHeader("User-Agent"), cartao);

        bloqueiaCartaoRepository.save(bloqueio);

        return ResponseEntity.ok().body("Cartão bloqueado. IdBloqueio: " + bloqueio.getIdBloqueio());
    }
}
