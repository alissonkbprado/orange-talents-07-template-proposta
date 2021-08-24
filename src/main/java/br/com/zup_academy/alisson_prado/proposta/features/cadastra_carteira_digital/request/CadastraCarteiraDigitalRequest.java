package br.com.zup_academy.alisson_prado.proposta.features.cadastra_carteira_digital.request;

import br.com.zup_academy.alisson_prado.proposta.exception.ApiErroException;
import br.com.zup_academy.alisson_prado.proposta.model.Cartao;
import br.com.zup_academy.alisson_prado.proposta.model.CarteiraDigital;
import br.com.zup_academy.alisson_prado.proposta.model.NomeCarteiraDigital;
import br.com.zup_academy.alisson_prado.proposta.repository.CarteiraDigitalRepository;
import br.com.zup_academy.alisson_prado.proposta.validacao.EnumNamePattern;
import org.springframework.http.HttpStatus;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class CadastraCarteiraDigitalRequest {

    @NotBlank @Email
    private String email;

    @NotNull @EnumNamePattern(regexp = "PAYPAL|SAMSUNG_PAY")
    private NomeCarteiraDigital carteiraDigital;

    public CadastraCarteiraDigitalRequest(String email, NomeCarteiraDigital carteiraDigital) {
        this.email = email;
        this.carteiraDigital = carteiraDigital;
    }

    public String getEmail() {
        return email;
    }

    public NomeCarteiraDigital getCarteiraDigital() {
        return carteiraDigital;
    }

    public CarteiraDigital toModel(Cartao cartao, CarteiraDigitalRepository carteiraDigitalRepository) {

        if(cartao.isBloqueado())
            throw new ApiErroException(HttpStatus.UNPROCESSABLE_ENTITY, "Operação não permitida, este cartão está bloqueado.");

        Boolean exists = carteiraDigitalRepository.existsByCartao_IdAndNome(cartao.getId(), this.carteiraDigital);

        if(exists)
            throw new ApiErroException(HttpStatus.UNPROCESSABLE_ENTITY, "Operação não permitida, este cartão já está cadastrado " +
                    "na carteira digital " + this.carteiraDigital);

        return new CarteiraDigital(this.carteiraDigital, this.email, cartao);
    }
}
