package br.com.zup_academy.alisson_prado.proposta.features.cadastra_biometria.controller;

import br.com.zup_academy.alisson_prado.proposta.exception.ApiErroException;
import br.com.zup_academy.alisson_prado.proposta.features.cadastra_biometria.request.CadastroBiometriaRequest;
import br.com.zup_academy.alisson_prado.proposta.model.Biometria;
import br.com.zup_academy.alisson_prado.proposta.model.Cartao;
import br.com.zup_academy.alisson_prado.proposta.repository.BiometriaRepository;
import br.com.zup_academy.alisson_prado.proposta.repository.CartaoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/propostas/biometrias")
public class CadastraBiometriaController {

    CartaoRepository cartaoRepository;
    BiometriaRepository biometriaRepository;

    public CadastraBiometriaController(CartaoRepository cartaoRepository, BiometriaRepository biometriaRepository) {
        this.cartaoRepository = cartaoRepository;
        this.biometriaRepository = biometriaRepository;
    }

    @PostMapping
    public ResponseEntity<?> cadastra(@Validated @RequestParam(name = "idCartao", required = true) @NotBlank String idCartao,
                                      @RequestBody @Valid CadastroBiometriaRequest request,
                                      UriComponentsBuilder uriBuilder){

        Optional<Cartao> optionalCartao = cartaoRepository.findByIdCartao(idCartao);

        if (optionalCartao.isEmpty())
            throw new ApiErroException(HttpStatus.NOT_FOUND, "Não foi encontrado cartão cadastrado no sistema com o identificador enviado.");

        Biometria biometria = request.toModel(optionalCartao.get());

        biometriaRepository.save(biometria);

        return ResponseEntity.created(uriBuilder.buildAndExpand("/{idBiometria}",
                biometria.getIdBiometria()).toUri()).body("Biometria cadastrada com sucesso");
    }

}
