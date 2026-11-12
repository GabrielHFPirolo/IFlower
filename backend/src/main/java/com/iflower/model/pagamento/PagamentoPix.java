package com.iflower.model.pagamento;

import com.iflower.model.Pedido;
import com.iflower.model.enums.TipoPagamento;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.math.BigDecimal;

@Entity
@DiscriminatorValue("PIX")
public class PagamentoPix extends PagamentoBase {

    @Column(name = "chave_pix", length = 140)
    private String chavePix;

    protected PagamentoPix() {
    }

    public PagamentoPix(Pedido pedido, String chavePix) {
        super(pedido);
        this.chavePix = chavePix;
    }

    /** Simulação: o Pix é confirmado na hora (não há integração com PSP). */
    @Override
    public void processarPagamento(BigDecimal valor) {
        registrarValor(valor);
        aprovar();
    }

    @Override
    public TipoPagamento getTipo() {
        return TipoPagamento.PIX;
    }

    public String getChavePix() { return chavePix; }
}
