package com.iflower.model;

import com.iflower.exception.RegraNegocioException;
import com.iflower.model.enums.StatusPagamento;
import com.iflower.model.pagamento.Pagamento;
import com.iflower.model.pagamento.PagamentoBase;
import com.iflower.model.pagamento.PagamentoBoleto;
import com.iflower.model.pagamento.PagamentoCartao;
import com.iflower.model.pagamento.PagamentoPix;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/** OCP: o código cliente trabalha com a abstração Pagamento, sem conhecer os tipos concretos. */
class PagamentoOcpTest {

    private final Pedido pedido = Pedido.novo(ModeloFabrica.cliente(1), ModeloFabrica.loja(1));
    private final BigDecimal valor = new BigDecimal("150.00");

    @Test
    void pixECartaoSaoAprovadosNaHora() {
        List<PagamentoBase> pagamentos = List.of(
                new PagamentoPix(pedido, "maria@teste.com"),
                new PagamentoCartao(pedido, "1234"));

        for (Pagamento p : pagamentos) {
            p.processarPagamento(valor);
        }
        for (PagamentoBase p : pagamentos) {
            assertEquals(StatusPagamento.APROVADO, p.getStatus());
            assertEquals(valor, p.getValor());
            assertNotNull(p.getDataPagamento());
        }
    }

    @Test
    void boletoFicaPendenteAteACompensacao() {
        PagamentoBoleto boleto = new PagamentoBoleto(pedido, "0".repeat(44));
        boleto.processarPagamento(valor);
        assertEquals(StatusPagamento.PENDENTE, boleto.getStatus());
        assertEquals(valor, boleto.getValor());

        boleto.confirmarCompensacao();
        assertEquals(StatusPagamento.APROVADO, boleto.getStatus());
        assertThrows(RegraNegocioException.class, boleto::confirmarCompensacao);
    }
}
