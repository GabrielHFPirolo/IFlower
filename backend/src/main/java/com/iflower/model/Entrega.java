package com.iflower.model;

import java.util.Date;

/**
 * SRP: a classe cuida somente do estado da própria entrega.
 * Não acessa banco, não altera pedido, não controla estoque.
 */
public class Entrega {

    private int id;
    private int pedidoId;
    private Date dataEntrega;
    private String horario;
    private String tipoRetirada;
    private boolean selecionada;
    private boolean cancelada;

    public Entrega(int id, int pedidoId, Date dataEntrega, String horario, String tipoRetirada) {
        this.id = id;
        this.pedidoId = pedidoId;
        this.dataEntrega = dataEntrega;
        this.horario = horario;
        this.tipoRetirada = tipoRetirada;
        this.selecionada = false;
        this.cancelada = false;
    }

    public int getId() {
        return id;
    }

    public int getPedidoId() {
        return pedidoId;
    }

    public Date getDataEntrega() {
        return dataEntrega;
    }

    public String getHorario() {
        return horario;
    }

    public String getTipoRetirada() {
        return tipoRetirada;
    }

    public boolean isSelecionada() {
        return selecionada;
    }

    public boolean isCancelada() {
        return cancelada;
    }

    public void setPedidoId(int pedidoId) {
        this.pedidoId = pedidoId;
    }

    public void setDataEntrega(Date dataEntrega) {
        this.dataEntrega = dataEntrega;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public void setTipoRetirada(String tipoRetirada) {
        this.tipoRetirada = tipoRetirada;
    }

    public void selecionarEntrega() {
        if (cancelada) {
            throw new IllegalStateException("Não é possível selecionar uma entrega cancelada.");
        }
        selecionada = true;
    }

    public void cancelar() {
        if (cancelada) {
            throw new IllegalStateException("A entrega já está cancelada.");
        }
        cancelada = true;
        selecionada = false;
    }
}
