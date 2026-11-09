package com.iflower.repository;

import com.iflower.model.pagamento.PagamentoBase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PagamentoRepository extends JpaRepository<PagamentoBase, Long> {
}
