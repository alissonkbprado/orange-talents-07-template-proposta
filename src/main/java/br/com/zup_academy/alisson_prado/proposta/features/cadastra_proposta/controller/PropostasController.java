package br.com.zup_academy.alisson_prado.proposta.features.cadastra_proposta.controller;

import br.com.zup_academy.alisson_prado.proposta.exception.ApiErroException;
import br.com.zup_academy.alisson_prado.proposta.features.cadastra_proposta.request.CadastraPropostaRequest;
import br.com.zup_academy.alisson_prado.proposta.features.cadastra_proposta.response.CadastraPropostaResponse;
import br.com.zup_academy.alisson_prado.proposta.features.cadastra_proposta.service.analise.SolicitaAnaliseClientFeign;
import br.com.zup_academy.alisson_prado.proposta.model.Proposta;
import br.com.zup_academy.alisson_prado.proposta.repository.PropostaRepository;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.opentracing.Span;
import io.opentracing.Tracer;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/api/v1/propostas")
public class PropostasController implements HealthIndicator {

    private PropostaRepository propostaRepository;
    private SolicitaAnaliseClientFeign clientFeign;
    private final MeterRegistry meterRegistry;
    private final Tracer tracer;

    public PropostasController(PropostaRepository propostaRepository, SolicitaAnaliseClientFeign clientFeign, MeterRegistry meterRegistry, Tracer tracer) {
        this.propostaRepository = propostaRepository;
        this.clientFeign = clientFeign;
        this.meterRegistry = meterRegistry;
        this.tracer = tracer;
    }

    @PostMapping
    public ResponseEntity<?> cadastraProposta(@RequestBody @Valid CadastraPropostaRequest request, UriComponentsBuilder uriBuilder){
        Proposta proposta = request.toModel();

        if(proposta.isDocumentoCadastrado(propostaRepository))
            throw new ApiErroException(HttpStatus.UNPROCESSABLE_ENTITY, "É permitido apenas uma proposta por CPF ou CNPJ!");

        proposta.avaliaRestricoes(clientFeign, tracer);
        propostaRepository.save(proposta);

        metricaContadorPropostaCriada();
        setTag(request, proposta);
        setBaggage(request, proposta);

        return ResponseEntity.created(uriBuilder
                .path("/{idProposta}")
                .buildAndExpand(proposta.getIdProposta()).toUri())
                .body(new CadastraPropostaResponse(proposta));
    }

    @GetMapping("/{idProposta}")
    public ResponseEntity<?> detalha(@PathVariable String idProposta){
        Optional<Proposta> optionalProposta = propostaRepository.findByIdProposta(idProposta);

        if(optionalProposta.isPresent())
            return ResponseEntity.ok(new CadastraPropostaResponse(optionalProposta.get()));

        return ResponseEntity.status(404).build();
    }



    @Override
    public Health health() {
        Map<String, Object> details = new HashMap<>();
        details.put("versão", "0.1");
        details.put("descrição", "Cadastro de propostas de cartões!");
        details.put("endereço", "localhost:8080/api/v1/propostas");

        return Health.status(Status.UP).withDetails(details).build();

    }

    // Métrica tipo Counter
    public void metricaContadorPropostaCriada() {
        Collection<Tag> tags = new ArrayList<>();
        tags.add(Tag.of("emissora", "Mastercard"));
        tags.add(Tag.of("banco", "Itaú"));

        Counter contadorDePropostasCriadas = this.meterRegistry.counter("proposta_criada", tags);

        contadorDePropostasCriadas.increment();
    }

    // Tag Tracing
    private void setTag(CadastraPropostaRequest request, Proposta proposta) {
        Span activeSpan = tracer.activeSpan();
        activeSpan.setTag("user.id", proposta.getIdProposta());
        activeSpan.setTag("user.email", request.getEmail());
    }

    // Baggage Tracing
    private void setBaggage(CadastraPropostaRequest request, Proposta proposta) {
        Span activeSpan = tracer.activeSpan();
        activeSpan.setBaggageItem("user.id", proposta.getIdProposta());
        activeSpan.setBaggageItem("user.email", request.getEmail());
    }
}
