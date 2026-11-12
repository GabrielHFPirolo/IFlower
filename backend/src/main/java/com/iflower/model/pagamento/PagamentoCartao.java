package com.iflower.model.pagamento;

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
}
