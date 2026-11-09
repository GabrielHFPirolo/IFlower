package com.iflower.model.pagamento;

<<<<<<< HEAD
import com.iflower.model.enums.TipoPagamento;

import java.math.BigDecimal;

/**
 * Abstração de pagamento (OCP): novas formas de pagamento implementam esta
 * interface sem alterar o código já existente.
 */
public interface Pagamento {

    void processarPagamento(BigDecimal valor);

    TipoPagamento getTipo();
=======
/**
 * Abstração de pagamento (OCP: novas formas de pagamento implementam esta
 * interface sem alterar o código já existente).
 */
public interface Pagamento {
    void processarPagamento(double valor);
>>>>>>> 30e1cb06bcc198f0b560a7b5897187a186905d87
}
