package br.com.zup_academy.alisson_prado.proposta.features.cadastra_proposta.controller;

import br.com.zup_academy.alisson_prado.proposta.features.cadastra_proposta.request.CadastraPropostaRequest;
import br.com.zup_academy.alisson_prado.proposta.features.cadastra_proposta.response.CadastraPropostaResponse;
import br.com.zup_academy.alisson_prado.proposta.model.Proposta;
import br.com.zup_academy.alisson_prado.proposta.repository.PropostaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/propostas")
public class PropostasController {

    PropostaRepository propostaRepository;

    public PropostasController(PropostaRepository propostaRepository) {
        this.propostaRepository = propostaRepository;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<?> cadastra(@RequestBody @Valid CadastraPropostaRequest request, UriComponentsBuilder uriBuilder){

        Proposta proposta = request.toModel();

        propostaRepository.save(proposta);

        return ResponseEntity.created(uriBuilder.buildAndExpand("/{uuid}",
                proposta.getIdUuid()).toUri()).body(new CadastraPropostaResponse(proposta));
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<?> detalha(@PathVariable String uuid){

        Optional<Proposta> optionalProposta = propostaRepository.findByIdUuid(uuid);

        if(optionalProposta.isPresent())
            return ResponseEntity.ok(new CadastraPropostaResponse(optionalProposta.get()));

        return ResponseEntity.status(404).build();

    }


}
