package br.com.zup_academy.alisson_prado.proposta.features.cadastra_proposta.controller;

import br.com.zup_academy.alisson_prado.proposta.exception.ApiErroException;
import br.com.zup_academy.alisson_prado.proposta.features.cadastra_proposta.request.CadastraPropostaRequest;
import br.com.zup_academy.alisson_prado.proposta.features.cadastra_proposta.response.CadastraPropostaResponse;
import br.com.zup_academy.alisson_prado.proposta.features.cadastra_proposta.service.SolicitaAnaliseClient;
import br.com.zup_academy.alisson_prado.proposta.model.Proposta;
import br.com.zup_academy.alisson_prado.proposta.repository.PropostaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/propostas")
public class PropostasController {

    private PropostaRepository propostaRepository;
    private SolicitaAnaliseClient solicitaAnaliseClient;

    public PropostasController(PropostaRepository propostaRepository, SolicitaAnaliseClient solicitaAnaliseClient) {
        this.propostaRepository = propostaRepository;
        this.solicitaAnaliseClient = solicitaAnaliseClient;
    }

    @PostMapping
    public ResponseEntity<?> cadastra(@RequestBody @Valid CadastraPropostaRequest request, UriComponentsBuilder uriBuilder){

        Proposta proposta = request.toModel();

        if(proposta.isDocumentoCadastrado(propostaRepository))
            throw new ApiErroException(HttpStatus.UNPROCESSABLE_ENTITY, "É permitido apenas uma proposta por CPF ou CNPJ!");

        proposta.avaliaRestricoes(solicitaAnaliseClient);
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
