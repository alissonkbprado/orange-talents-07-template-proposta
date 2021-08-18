package br.com.zup_academy.alisson_prado.proposta.repository;

import br.com.zup_academy.alisson_prado.proposta.model.Proposta;
import br.com.zup_academy.alisson_prado.proposta.model.StatusProposta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PropostaRepository extends JpaRepository<Proposta, Long> {
    Optional<Proposta> findByIdProposta(String uuid);

    boolean existsByClienteDocumento(String documento);

    List<Proposta> findByStatus(StatusProposta statusProposta);
}
