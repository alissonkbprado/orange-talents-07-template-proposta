package br.com.zup_academy.alisson_prado.proposta.features.cadastra_biometria.request;

import br.com.zup_academy.alisson_prado.proposta.compartilhado.ConverteBase64;
import br.com.zup_academy.alisson_prado.proposta.exception.ApiErroException;
import br.com.zup_academy.alisson_prado.proposta.model.Biometria;
import br.com.zup_academy.alisson_prado.proposta.model.Cartao;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.HttpStatus;

import javax.validation.constraints.NotNull;
import java.util.Optional;

public class CadastroBiometriaRequest {

    @NotNull
    private String biometriaCodificada;

    @JsonCreator
    public CadastroBiometriaRequest(@JsonProperty("biometriaCodificada") String biometriaCodificada) {
        this.biometriaCodificada = biometriaCodificada;
    }

    public Biometria toModel(Optional<Cartao> optionalCartao){
        if (optionalCartao.isEmpty())
            throw new ApiErroException(HttpStatus.NOT_FOUND, "Não foi encontrado cartão cadastrado no sistema com o identificador enviado.");

        if(optionalCartao.get().isBloqueado())
            throw new ApiErroException(HttpStatus.UNPROCESSABLE_ENTITY, "Este cartão está bloqueado.");

        try {
            return new Biometria(ConverteBase64.decodifica(this.biometriaCodificada), optionalCartao.get());
        } catch (Exception e) {
            throw new ApiErroException(HttpStatus.BAD_REQUEST, "A biometria não foi enviada ou está em formato inválido");
        }
    }
}
