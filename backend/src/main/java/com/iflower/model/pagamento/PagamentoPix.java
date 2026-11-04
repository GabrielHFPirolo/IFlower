package com.iflower.model.pagamento;

public class PagamentoPix extends PagamentoBase {

    private String chavePix;
    private double valor;

    public PagamentoPix(int id, int pedidoId, String chavePix) {
        super(id, pedidoId);
        this.chavePix = chavePix;
    }

    public String getChavePix() {
        return chavePix;
    }

    public void setChavePix(String chavePix) {
        this.chavePix = chavePix;
    }

    public double getValor() {
        return valor;
    }

    @Override
    public void processarPagamento(double valor) {
        this.valor = valor;
    }
}
