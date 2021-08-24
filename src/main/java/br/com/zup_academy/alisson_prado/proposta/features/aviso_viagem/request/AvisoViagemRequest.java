package br.com.zup_academy.alisson_prado.proposta.features.aviso_viagem.request;

import br.com.zup_academy.alisson_prado.proposta.exception.ApiErroException;
import br.com.zup_academy.alisson_prado.proposta.model.AvisoViagem;
import br.com.zup_academy.alisson_prado.proposta.model.Cartao;
import br.com.zup_academy.alisson_prado.proposta.repository.AvisoViagemRepository;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

import static br.com.zup_academy.alisson_prado.proposta.compartilhado.GetIPAdress.getClientIp;

public class AvisoViagemRequest {

    @NotBlank
    private String paisDestino;

    @NotNull @Future
	private LocalDate dataTermino;

    public AvisoViagemRequest(String paisDestino, LocalDate dataTermino) {
        this.paisDestino = paisDestino;
        this.dataTermino = dataTermino;
    }

    public AvisoViagem toModel(Cartao cartao, HttpServletRequest httpServletRequest, AvisoViagemRepository avisoViagemRepository) {
        Integer existe = avisoViagemRepository.existsAvisoViagem(cartao.getId(), this.paisDestino.trim().toUpperCase(), this.dataTermino);

        if(existe != null)
            throw new ApiErroException(HttpStatus.UNPROCESSABLE_ENTITY, "Já existe um aviso viagem cadastrado para este cartão com o pais e data dentro do período informado.");

        if(cartao.isBloqueado())
            throw new ApiErroException(HttpStatus.UNPROCESSABLE_ENTITY, "Este cartão está bloqueado.");

        return new AvisoViagem(this.paisDestino, this.dataTermino, getClientIp(httpServletRequest), httpServletRequest.getHeader("User-Agent"), cartao);
    }

    public String getPaisDestino() {
        return paisDestino;
    }

    public LocalDate getDataTermino() {
        return dataTermino;
    }
}
