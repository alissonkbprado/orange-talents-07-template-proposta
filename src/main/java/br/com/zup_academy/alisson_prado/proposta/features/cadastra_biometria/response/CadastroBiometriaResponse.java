package br.com.zup_academy.alisson_prado.proposta.features.cadastra_biometria.response;

import br.com.zup_academy.alisson_prado.proposta.compartilhado.ConverteBase64;
import br.com.zup_academy.alisson_prado.proposta.exception.ApiErroException;
import org.springframework.http.HttpStatus;

public class CadastroBiometriaResponse {

    private String biometriaCodificada;

    public CadastroBiometriaResponse(String biometriaDecodificada) {
        try {
            this.biometriaCodificada = ConverteBase64.codifica(biometriaDecodificada);
        } catch (Exception e) {
            throw new ApiErroException(HttpStatus.INTERNAL_SERVER_ERROR, "Falho ao converter a biometria cadastrada.");
        }
    }

    public String getBiometriaCodificada() {
        return biometriaCodificada;
    }


}
