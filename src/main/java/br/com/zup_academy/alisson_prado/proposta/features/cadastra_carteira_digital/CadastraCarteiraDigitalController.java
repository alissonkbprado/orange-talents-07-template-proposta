package br.com.zup_academy.alisson_prado.proposta.features.cadastra_carteira_digital;

import br.com.zup_academy.alisson_prado.proposta.exception.ApiErroException;
import br.com.zup_academy.alisson_prado.proposta.features.cadastra_carteira_digital.service.CadastraCarteiraDigitalService;
import br.com.zup_academy.alisson_prado.proposta.model.Cartao;
import br.com.zup_academy.alisson_prado.proposta.model.CarteiraDigital;
import br.com.zup_academy.alisson_prado.proposta.repository.CartaoRepository;
import br.com.zup_academy.alisson_prado.proposta.repository.CarteiraDigitalRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.Optional;

import static br.com.zup_academy.alisson_prado.proposta.compartilhado.ValidaUuid.isUuidNotValid;

@RestController
public class CadastraCarteiraDigitalController {

    private CartaoRepository cartaoRepository;
    private CarteiraDigitalRepository carteiraDigitalRepository;
    private CadastraCarteiraDigitalService cadastraCarteiraDigitalService;

    public CadastraCarteiraDigitalController(CartaoRepository cartaoRepository,
                                             CarteiraDigitalRepository carteiraDigitalRepository,
                                             CadastraCarteiraDigitalService cadastraCarteiraDigitalService) {
        this.cartaoRepository = cartaoRepository;
        this.carteiraDigitalRepository = carteiraDigitalRepository;
        this.cadastraCarteiraDigitalService = cadastraCarteiraDigitalService;
    }

    @PostMapping("/api/v1/cartao/{idCartao}/carteira_digital")
    public ResponseEntity<?> bloqueia(@PathVariable @NotBlank String idCartao,
                                      @RequestBody @Valid CadastraCarteiraDigitalRequest request,
                                      UriComponentsBuilder uriBuilder){

        if(idCartao == null || idCartao.isBlank() || isUuidNotValid(idCartao))
            throw new ApiErroException(HttpStatus.BAD_REQUEST, "Identificador do cartão não foi enviado ou é inválido.");

        Optional<Cartao> optionalCartao = cartaoRepository.findByIdCartao(idCartao);

        if(optionalCartao.isEmpty())
            return ResponseEntity.status(404).build();

        Cartao cartao = optionalCartao.get();

        CarteiraDigital carteiraDigital = request.toModel(cartao, carteiraDigitalRepository);

        if (!cadastraCarteiraDigitalService.enviaApi(carteiraDigital))
            throw new ApiErroException(HttpStatus.SERVICE_UNAVAILABLE, "Não foi possível efetuar o cadastro da carteira digital no momento " +
                    "devido a falha de comunicação com a operadora de cartão de crédito. Por favor tente novamente mais tarde.");

        carteiraDigitalRepository.save(carteiraDigital);

        return ResponseEntity.created(uriBuilder
                .path("/api/v1/cartao/{idCartao}/carteira_digital/{idCarteiraDigital}")
                .buildAndExpand(cartao.getIdCartao(), carteiraDigital.getIdCarteiraDigital()).toUri())
                .body("Carteira Digital " + request.getCarteiraDigital() + " cadastrada com sucesso");
    }
}
