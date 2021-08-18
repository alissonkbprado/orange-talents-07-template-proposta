package br.com.zup_academy.alisson_prado.proposta.repository;

import br.com.zup_academy.alisson_prado.proposta.model.Cartao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartaoRepository extends JpaRepository<Cartao, Long> {
}
