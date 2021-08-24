package br.com.zup_academy.alisson_prado.proposta.repository;

import br.com.zup_academy.alisson_prado.proposta.model.CarteiraDigital;
import br.com.zup_academy.alisson_prado.proposta.model.NomeCarteiraDigital;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarteiraDigitalRepository extends JpaRepository<CarteiraDigital, Long> {
    Boolean existsByCartao_IdAndNome(Long id, NomeCarteiraDigital carteiraDigital);
}
