package br.com.zup_academy.alisson_prado.proposta.features.status_proposta;

import br.com.zup_academy.alisson_prado.proposta.features.cadastra_proposta.response.StatusPropostaResponse;
import br.com.zup_academy.alisson_prado.proposta.model.Proposta;
import br.com.zup_academy.alisson_prado.proposta.repository.PropostaRepository;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.Timer;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@RestController
public class ConsultaStatusProposta {

    private PropostaRepository propostaRepository;
    private final MeterRegistry meterRegistry;

    public ConsultaStatusProposta(PropostaRepository propostaRepository, MeterRegistry meterRegistry) {
        this.propostaRepository = propostaRepository;
        this.meterRegistry = meterRegistry;
    }

    @GetMapping("/api/v1/propostas")
    public ResponseEntity<?> status(@RequestParam(name = "idProposta", required = true) String idProposta){
        Collection<Tag> tags = new ArrayList<>();
        tags.add(Tag.of("emissora", "Mastercard"));
        tags.add(Tag.of("banco", "Itaú"));

        Timer timerConsultarProposta = this.meterRegistry.timer("proposta_consulta_status", tags);

        return timerConsultarProposta.record(() -> {
            Optional<Proposta> optionalProposta = propostaRepository.findByIdProposta(idProposta);

            if(optionalProposta.isPresent())
                return ResponseEntity.ok(new StatusPropostaResponse(optionalProposta.get()));

            return ResponseEntity.status(404).build();
        });
    }
}
