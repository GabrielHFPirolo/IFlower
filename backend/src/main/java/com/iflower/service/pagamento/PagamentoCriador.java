package com.iflower.service.pagamento;

import com.iflower.dto.PagamentoDTO;
import com.iflower.model.Pedido;
import com.iflower.model.enums.TipoPagamento;
import com.iflower.model.pagamento.PagamentoBase;

/**
 * Estratégia de criação de um tipo de pagamento. O PagamentoService recebe TODAS as
 * implementações via injeção e escolhe pelo tipo: para suportar uma nova forma de
 * pagamento basta criar a entidade + um novo PagamentoCriador (@Component),
 * sem alterar o service (princípio Aberto/Fechado).
 */
public interface PagamentoCriador {

    TipoPagamento tipo();

    PagamentoBase criar(Pedido pedido, PagamentoDTO.Request dados);
}
