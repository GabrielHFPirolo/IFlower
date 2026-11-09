package com.iflower.repository;

import com.iflower.model.Pedido;
import com.iflower.model.enums.StatusPedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByClienteIdOrderByDataPedidoDesc(Long clienteId);

    List<Pedido> findByLojaIdOrderByDataPedidoDesc(Long lojaId);

    boolean existsByClienteIdAndLojaIdAndStatus(Long clienteId, Long lojaId, StatusPedido status);
}
