package br.com.zup_academy.alisson_prado.proposta.repository;

import br.com.zup_academy.alisson_prado.proposta.model.Bloqueio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BloqueiaCartaoRepository extends JpaRepository<Bloqueio, Long> {
}
