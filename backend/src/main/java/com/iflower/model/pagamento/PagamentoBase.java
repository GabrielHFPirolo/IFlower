package com.iflower.model.pagamento;

public abstract class PagamentoBase implements Pagamento {

    private int id;
    private int pedidoId;

    public PagamentoBase(int id, int pedidoId) {
        this.id = id;
        this.pedidoId = pedidoId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(int pedidoId) {
        this.pedidoId = pedidoId;
    }
}
