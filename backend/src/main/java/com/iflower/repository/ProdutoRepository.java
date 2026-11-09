package com.iflower.repository;

import com.iflower.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    List<Produto> findByLojaIdAndAtivoTrueOrderByNome(Long lojaId);
}
