package com.iflower.model.pagamento;

<<<<<<< HEAD
import com.iflower.model.Pedido;
import com.iflower.model.enums.TipoPagamento;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.math.BigDecimal;

/** Só os 4 últimos dígitos são guardados: o número completo do cartão nunca é persistido. */
@Entity
@DiscriminatorValue("CARTAO")
public class PagamentoCartao extends PagamentoBase {

    @Column(name = "ultimos_digitos", length = 4)
    private String ultimosDigitos;

    protected PagamentoCartao() {
    }

    public PagamentoCartao(Pedido pedido, String ultimosDigitos) {
        super(pedido);
        this.ultimosDigitos = ultimosDigitos;
    }

    /** Simulação: a operadora "aprova" imediatamente (não há integração com gateway). */
    @Override
    public void processarPagamento(BigDecimal valor) {
        registrarValor(valor);
        aprovar();
    }

    @Override
    public TipoPagamento getTipo() {
        return TipoPagamento.CARTAO;
    }

    public String getUltimosDigitos() { return ultimosDigitos; }
=======
public class PagamentoCartao extends PagamentoBase {

    private String ultimosDigitos;
    private double valor;

    public PagamentoCartao(int id, int pedidoId, String ultimosDigitos) {
        super(id, pedidoId);
        this.ultimosDigitos = ultimosDigitos;
    }

    public String getUltimosDigitos() {
        return ultimosDigitos;
    }

    public void setUltimosDigitos(String ultimosDigitos) {
        this.ultimosDigitos = ultimosDigitos;
    }

    public double getValor() {
        return valor;
    }

    @Override
    public void processarPagamento(double valor) {
        this.valor = valor;
    }
>>>>>>> 30e1cb06bcc198f0b560a7b5897187a186905d87
}
