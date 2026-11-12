package com.iflower.model.pagamento;

import com.iflower.model.Pedido;
import com.iflower.model.enums.StatusPagamento;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Centraliza o que é comum a todo pagamento (id, pedido, valor, status), evitando
 * repetição nas subclasses. Persistência com herança SINGLE_TABLE (tabela pagamento,
 * coluna discriminadora "tipo").
 */
@Entity
@Table(name = "pagamento")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo", discriminatorType = DiscriminatorType.STRING, length = 10)
public abstract class PagamentoBase implements Pagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal valor = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private StatusPagamento status = StatusPagamento.PENDENTE;

    @Column(name = "data_pagamento")
    private LocalDateTime dataPagamento;

    protected PagamentoBase() {
    }

    protected PagamentoBase(Pedido pedido) {
        this.pedido = pedido;
    }

    protected void registrarValor(BigDecimal valor) {
        this.valor = valor;
    }

    protected void aprovar() {
        this.status = StatusPagamento.APROVADO;
        this.dataPagamento = LocalDateTime.now();
    }

    protected void recusar() {
        this.status = StatusPagamento.RECUSADO;
    }

    public Long getId() { return id; }
    public Pedido getPedido() { return pedido; }
    public BigDecimal getValor() { return valor; }
    public StatusPagamento getStatus() { return status; }
    public LocalDateTime getDataPagamento() { return dataPagamento; }
}
