package com.iflower.repository;

import com.iflower.model.Avaliacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AvaliacaoRepository extends JpaRepository<Avaliacao, Long> {

    List<Avaliacao> findByLojaIdOrderByDataAvaliacaoDesc(Long lojaId);

    Optional<Avaliacao> findByClienteIdAndLojaId(Long clienteId, Long lojaId);

    long countByLojaId(Long lojaId);

    @Query("select avg(a.nota) from Avaliacao a where a.loja.id = :lojaId")
    Double mediaPorLoja(@Param("lojaId") Long lojaId);
}
