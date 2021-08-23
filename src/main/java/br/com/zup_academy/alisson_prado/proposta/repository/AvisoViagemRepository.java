package br.com.zup_academy.alisson_prado.proposta.repository;

import br.com.zup_academy.alisson_prado.proposta.model.AvisoViagem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;

public interface AvisoViagemRepository extends JpaRepository<AvisoViagem, Long> {
    @Query(value = "SELECT 1 FROM AVISO_VIAGEM WHERE CARTAO_ID = ?1 AND PAIS_DESTINO = ?2 AND DATA_TERMINO >= ?3", nativeQuery = true)
    Integer existsAvisoViagem(Long id, String paisDestino, LocalDate dataTermino);
}
