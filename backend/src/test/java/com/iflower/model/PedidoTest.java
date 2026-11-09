package com.iflower.model;

import com.iflower.exception.RegraNegocioException;
import com.iflower.model.enums.StatusPedido;
import com.iflower.model.enums.TipoRetirada;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PedidoTest {

    private Pedido pedidoCom(TipoRetirada tipo) {
        Loja loja = ModeloFabrica.loja(1);
        Pedido pedido = Pedido.novo(ModeloFabrica.cliente(1), loja);
        Produto p = ModeloFabrica.produto(1, loja, "50.00", false);
        pedido.adicionarItem(new ItemPedido(p, 2, p.getPreco(), null));
        Entrega entrega = new Entrega(pedido, LocalDate.now().plusDays(1), LocalTime.NOON, tipo,
                tipo == TipoRetirada.ENTREGA ? ModeloFabrica.endereco() : null);
        entrega.selecionarEntrega();
        pedido.definirEntrega(entrega);
        return pedido;
    }

    @Test
    void calculaTotalPelosItens() {
        Pedido pedido = pedidoCom(TipoRetirada.ENTREGA);
        assertEquals(new BigDecimal("100.00"), pedido.getValorTotal());
        assertEquals(StatusPedido.AGUARDANDO_PAGAMENTO, pedido.getStatus());
    }

    @Test
    void fluxoCompletoDeEntrega() {
        Pedido pedido = pedidoCom(TipoRetirada.ENTREGA);
        pedido.confirmarPagamento();
        pedido.atualizarStatus(StatusPedido.EM_PREPARO);
        pedido.atualizarStatus(StatusPedido.SAIU_PARA_ENTREGA);
        pedido.atualizarStatus(StatusPedido.ENTREGUE);
        assertEquals(StatusPedido.ENTREGUE, pedido.getStatus());
    }

    @Test
    void fluxoCompletoDeRetiradaNaLoja() {
        Pedido pedido = pedidoCom(TipoRetirada.RETIRADA_NA_LOJA);
        pedido.confirmarPagamento();
        pedido.atualizarStatus(StatusPedido.EM_PREPARO);
        pedido.atualizarStatus(StatusPedido.PRONTO_PARA_RETIRADA);
        pedido.atualizarStatus(StatusPedido.ENTREGUE);
        assertEquals(StatusPedido.ENTREGUE, pedido.getStatus());
    }

    @Test
    void naoPermitePularEtapas() {
        Pedido pedido = pedidoCom(TipoRetirada.ENTREGA);
        assertThrows(RegraNegocioException.class, () -> pedido.atualizarStatus(StatusPedido.ENTREGUE));
        assertThrows(RegraNegocioException.class, () -> pedido.atualizarStatus(StatusPedido.EM_PREPARO));
    }

    @Test
    void retiradaNaoPodeSairParaEntregaENemEntregaFicaProntaParaRetirada() {
        Pedido retirada = pedidoCom(TipoRetirada.RETIRADA_NA_LOJA);
        retirada.confirmarPagamento();
        retirada.atualizarStatus(StatusPedido.EM_PREPARO);
        assertThrows(RegraNegocioException.class, () -> retirada.atualizarStatus(StatusPedido.SAIU_PARA_ENTREGA));

        Pedido entrega = pedidoCom(TipoRetirada.ENTREGA);
        entrega.confirmarPagamento();
        entrega.atualizarStatus(StatusPedido.EM_PREPARO);
        assertThrows(RegraNegocioException.class, () -> entrega.atualizarStatus(StatusPedido.PRONTO_PARA_RETIRADA));
    }

    @Test
    void cancelarTambemCancelaAEntrega() {
        Pedido pedido = pedidoCom(TipoRetirada.ENTREGA);
        pedido.cancelar();
        assertEquals(StatusPedido.CANCELADO, pedido.getStatus());
        assertTrue(pedido.getEntrega().isCancelada());
        assertThrows(RegraNegocioException.class, () -> pedido.atualizarStatus(StatusPedido.CONFIRMADO));
    }

    @Test
    void clienteSoCancelaAteConfirmado() {
        assertTrue(StatusPedido.AGUARDANDO_PAGAMENTO.cancelavelPeloCliente());
        assertTrue(StatusPedido.CONFIRMADO.cancelavelPeloCliente());
        assertTrue(!StatusPedido.EM_PREPARO.cancelavelPeloCliente());
    }
}
