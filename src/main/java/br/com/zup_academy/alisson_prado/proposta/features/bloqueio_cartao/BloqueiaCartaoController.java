package br.com.zup_academy.alisson_prado.proposta.features.bloqueio_cartao;

import br.com.zup_academy.alisson_prado.proposta.exception.ApiErroException;
import br.com.zup_academy.alisson_prado.proposta.model.Bloqueio;
import br.com.zup_academy.alisson_prado.proposta.model.Cartao;
import br.com.zup_academy.alisson_prado.proposta.model.StatusBloqueio;
import br.com.zup_academy.alisson_prado.proposta.repository.BloqueiaCartaoRepository;
import br.com.zup_academy.alisson_prado.proposta.repository.CartaoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotBlank;
import java.util.Optional;

import static br.com.zup_academy.alisson_prado.proposta.compartilhado.GetIPAdress.getClientIp;
import static br.com.zup_academy.alisson_prado.proposta.compartilhado.ValidaUuid.isUuidNotValid;

@RestController
public class BloqueiaCartaoController {

    CartaoRepository cartaoRepository;
    BloqueiaCartaoRepository bloqueiaCartaoRepository;
    BloqueiaCartaoClientFeign bloqueiaCartaoClientFeign;

    public BloqueiaCartaoController(CartaoRepository cartaoRepository,
                                    BloqueiaCartaoRepository bloqueiaCartaoRepository,
                                    BloqueiaCartaoClientFeign bloqueiaCartaoClientFeign) {
        this.cartaoRepository = cartaoRepository;
        this.bloqueiaCartaoRepository = bloqueiaCartaoRepository;
        this.bloqueiaCartaoClientFeign = bloqueiaCartaoClientFeign;
    }

    @PostMapping("/api/v1/cartao/{idCartao}/bloqueia")
    public ResponseEntity<?> bloqueia(@PathVariable @NotBlank String idCartao, HttpServletRequest request){

        if(idCartao == null || idCartao.isBlank() || isUuidNotValid(idCartao))
            throw new ApiErroException(HttpStatus.BAD_REQUEST, "Identificador do cartão não foi enviado ou é inválido.");

        Optional<Cartao> optionalCartao = cartaoRepository.findByIdCartao(idCartao);

        if(optionalCartao.isEmpty())
            return ResponseEntity.status(404).build();

        Cartao cartao = optionalCartao.get();

        if(cartao.isBloqueado())
            throw new ApiErroException(HttpStatus.UNPROCESSABLE_ENTITY, "O cartão já está bloqueado.");

        if(cartao.bloqueia(bloqueiaCartaoClientFeign)){
            Bloqueio bloqueio = new Bloqueio(getClientIp(request), request.getHeader("User-Agent"), StatusBloqueio.SUCESSO, cartao);
            bloqueiaCartaoRepository.save(bloqueio);
            return ResponseEntity.ok().body("Cartão bloqueado com sucesso. IdBloqueio: " + bloqueio.getIdBloqueio());
        }else {
            Bloqueio bloqueio = new Bloqueio(getClientIp(request), request.getHeader("User-Agent"), StatusBloqueio.FALHA, cartao);
            bloqueiaCartaoRepository.save(bloqueio);
            throw new ApiErroException(HttpStatus.SERVICE_UNAVAILABLE, "Não foi possível efetuar o bloqueio no momento devido a falha de comunicação " +
                    "com a operadora de cartão de crédito. Não se preocupe que nosso sistema está programado para efetuar o bloqueio de forma automática " +
                    "quando conseguirmos restabelecer comunicação com a operadora de cartão de crédito.");
        }
    }
}
