package br.com.zup_academy.alisson_prado.proposta.metricas;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;

@Component
public class Metricas {

    private final MeterRegistry meterRegistry;
    private Counter contadorDeFalhaAvisoViagem;
    private Counter contadorDeFalhaCadastroCarteiraDigital;
    private Counter contadorDePropostasCriadas;

    public Metricas(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    public void incrementaFalhaAvisoViagem(String emissora, String banco) {
        Collection<Tag> tags = new ArrayList<>();
        tags.add(Tag.of("emissora", emissora));
        tags.add(Tag.of("banco", banco));

        contadorDeFalhaAvisoViagem = this.meterRegistry.counter("proposta_aviso_viagem_erro", tags);

        contadorDeFalhaAvisoViagem.increment();
    }

    public void incrementaFalhaCarteiraDigital(String carteiraDigital) {
        Collection<Tag> tags = new ArrayList<>();
        tags.add(Tag.of("Carteira", carteiraDigital));

        contadorDeFalhaCadastroCarteiraDigital = this.meterRegistry.counter("proposta_cadastra_carteira_digital_erro", tags);

        contadorDeFalhaCadastroCarteiraDigital.increment();
    }

    // Métrica tipo Counter
    public void incrementaPropostaCriada(String emissora, String banco) {
        Collection<Tag> tags = new ArrayList<>();
        tags.add(Tag.of("emissora", emissora));
        tags.add(Tag.of("banco", banco));

        contadorDePropostasCriadas = this.meterRegistry.counter("proposta_criada", tags);

        contadorDePropostasCriadas.increment();
    }
}
