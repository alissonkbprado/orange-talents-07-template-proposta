package br.com.zup_academy.alisson_prado.proposta.features.novo_cartao;

import br.com.zup_academy.alisson_prado.proposta.model.Cartao;
import br.com.zup_academy.alisson_prado.proposta.model.Proposta;
import br.com.zup_academy.alisson_prado.proposta.repository.PropostaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Optional;

public class NovoCartaoResponse {

    private final Logger logger = LoggerFactory.getLogger(NovoCartaoResponse.class);

    @NotBlank
    private String id;
    @NotNull
    private LocalDateTime emitidoEm;
    @NotBlank
    private String titular;
    @NotBlank
    private String idProposta;

    public NovoCartaoResponse(String id, LocalDateTime emitidoEm, String titular, String idProposta) {
        this.id = id;
        this.emitidoEm = emitidoEm;
        this.titular = titular;
        this.idProposta = idProposta;
    }

    public Cartao toModel(PropostaRepository propostaRepository){
        Optional<Proposta> optionalProposta = propostaRepository.findByIdProposta(idProposta);
        if (optionalProposta.isPresent())
            return new Cartao(id, emitidoEm, optionalProposta.get().getCliente());

        logger.error("O idProposta retornado não foi encontrado em nossa base de dados: {}", idProposta);

        return null;
    }
}
