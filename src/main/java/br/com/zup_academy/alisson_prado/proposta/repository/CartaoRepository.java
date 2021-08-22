package br.com.zup_academy.alisson_prado.proposta.repository;

import br.com.zup_academy.alisson_prado.proposta.model.Cartao;
import br.com.zup_academy.alisson_prado.proposta.model.StatusCartao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartaoRepository extends JpaRepository<Cartao, Long> {
    Optional<Cartao> findByIdCartao(String idCartao);

    List<Cartao> findFirst100ByStatus(StatusCartao aguardandoBloqueio);
}
