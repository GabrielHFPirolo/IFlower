package com.iflower.repository;

import com.iflower.model.Loja;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LojaRepository extends JpaRepository<Loja, Long> {

    Optional<Loja> findByVendedorId(Long vendedorId);

    boolean existsByCnpj(String cnpj);

    /**
     * Busca por nome parcial e/ou cidade/UF. Parâmetros vazios ("") significam "sem filtro";
     * o service nunca envia null (evita problema de tipo de parâmetro nulo no PostgreSQL).
     */
    @Query("""
            select l from Loja l
            where l.ativa = true
              and lower(l.nome) like lower(concat('%', :nome, '%'))
              and (:cidade = '' or lower(l.endereco.cidade) = lower(:cidade))
              and (:uf = '' or upper(l.endereco.uf) = upper(:uf))
            order by l.nome
            """)
    List<Loja> buscar(@Param("nome") String nome, @Param("cidade") String cidade, @Param("uf") String uf);
}
