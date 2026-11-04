package com.iflower.model.pagamento;

public class PagamentoBoleto extends PagamentoBase {

    private String codigoBarras;
    private double valor;

    public PagamentoBoleto(int id, int pedidoId, String codigoBarras) {
        super(id, pedidoId);
        this.codigoBarras = codigoBarras;
    }

    public String getCodigoBarras() {
        return codigoBarras;
    }

    public void setCodigoBarras(String codigoBarras) {
        this.codigoBarras = codigoBarras;
    }

    public double getValor() {
        return valor;
    }

    @Override
    public void processarPagamento(double valor) {
        this.valor = valor;
    }
}
