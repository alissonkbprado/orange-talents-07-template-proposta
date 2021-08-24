package br.com.zup_academy.alisson_prado.proposta.features.cadastra_biometria.controller;

import br.com.zup_academy.alisson_prado.proposta.exception.ApiErroException;
import br.com.zup_academy.alisson_prado.proposta.features.cadastra_biometria.request.CadastroBiometriaRequest;
import br.com.zup_academy.alisson_prado.proposta.features.cadastra_biometria.response.CadastroBiometriaResponse;
import br.com.zup_academy.alisson_prado.proposta.model.Biometria;
import br.com.zup_academy.alisson_prado.proposta.model.Cartao;
import br.com.zup_academy.alisson_prado.proposta.repository.BiometriaRepository;
import br.com.zup_academy.alisson_prado.proposta.repository.CartaoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.Optional;

import static br.com.zup_academy.alisson_prado.proposta.compartilhado.ValidaUuid.isUuidNotValid;

@RestController
@RequestMapping("/api/v1/cartao")
public class CadastraBiometriaController {

    private CartaoRepository cartaoRepository;
    private BiometriaRepository biometriaRepository;

    public CadastraBiometriaController(CartaoRepository cartaoRepository, BiometriaRepository biometriaRepository) {
        this.cartaoRepository = cartaoRepository;
        this.biometriaRepository = biometriaRepository;
    }

    @PostMapping("/biometria")
    @Transactional
    public ResponseEntity<?> cadastra(@RequestParam(name = "idCartao", required = true) @NotBlank String idCartao,
                                      @RequestBody @Valid CadastroBiometriaRequest request,
                                      UriComponentsBuilder uriBuilder){

        if(idCartao == null || idCartao.isBlank() || isUuidNotValid(idCartao))
            throw new ApiErroException(HttpStatus.BAD_REQUEST, "Identificador do cartão não foi enviado ou é inválido.");

        Optional<Cartao> optionalCartao = cartaoRepository.findByIdCartao(idCartao);

        if (optionalCartao.isEmpty())
            throw new ApiErroException(HttpStatus.NOT_FOUND, "Não foi encontrado cartão cadastrado no sistema com o identificador enviado.");

        if(optionalCartao.get().isBloqueado())
            throw new ApiErroException(HttpStatus.UNPROCESSABLE_ENTITY, "Este cartão está bloqueado.");

        Biometria biometria = request.toModel(optionalCartao.get());

        biometriaRepository.save(biometria);

        return ResponseEntity.created(uriBuilder
                .path("/biometria/{idBiometria}")
                .buildAndExpand(biometria.getIdBiometria()).toUri())
                .body("Biometria cadastrada com sucesso");
    }

    @GetMapping("/biometria/{idBiometria}")
    public ResponseEntity<?> detalha(@PathVariable @NotBlank String idBiometria){

        Optional<Biometria> optionalBiometria = biometriaRepository.findByIdBiometria(idBiometria);

        if(optionalBiometria.isPresent())
            return ResponseEntity.ok(new CadastroBiometriaResponse(optionalBiometria.get().getBiometria()));

        return ResponseEntity.status(404).build();
    }

}
