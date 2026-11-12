package com.iflower.model.pagamento;

import com.iflower.exception.RegraNegocioException;
import com.iflower.model.Pedido;
import com.iflower.model.enums.StatusPagamento;
import com.iflower.model.enums.TipoPagamento;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.math.BigDecimal;

@Entity
@DiscriminatorValue("BOLETO")
public class PagamentoBoleto extends PagamentoBase {

    @Column(name = "codigo_barras", length = 48)
    private String codigoBarras;

    protected PagamentoBoleto() {
    }

    public PagamentoBoleto(Pedido pedido, String codigoBarras) {
        super(pedido);
        this.codigoBarras = codigoBarras;
    }

    /** O boleto nasce PENDENTE e só é aprovado quando a compensação é confirmada. */
    @Override
    public void processarPagamento(BigDecimal valor) {
        registrarValor(valor);
    }

    public void confirmarCompensacao() {
        if (getStatus() != StatusPagamento.PENDENTE) {
            throw new RegraNegocioException("Somente boletos pendentes podem ser confirmados.");
        }
        aprovar();
    }

    @Override
    public TipoPagamento getTipo() {
        return TipoPagamento.BOLETO;
    }

    public String getCodigoBarras() { return codigoBarras; }
}
