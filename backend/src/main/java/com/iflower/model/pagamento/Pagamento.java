package com.iflower.model.pagamento;

import com.iflower.model.enums.TipoPagamento;

import java.math.BigDecimal;

/**
 * Abstração de pagamento (OCP): novas formas de pagamento implementam esta
 * interface sem alterar o código já existente.
 */
public interface Pagamento {

    void processarPagamento(BigDecimal valor);

    TipoPagamento getTipo();
}
