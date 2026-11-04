package com.iflower.model.pagamento;

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
}
