package br.com.zup_academy.alisson_prado.proposta.repository;

import br.com.zup_academy.alisson_prado.proposta.model.Biometria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BiometriaRepository extends JpaRepository<Biometria, Long> {
    Optional<Biometria> findByIdBiometria(String idBiometria);
}
