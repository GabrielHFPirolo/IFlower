package com.iflower.model.pagamento;

/**
 * Abstração de pagamento (OCP: novas formas de pagamento implementam esta
 * interface sem alterar o código já existente).
 */
public interface Pagamento {
    void processarPagamento(double valor);
}
