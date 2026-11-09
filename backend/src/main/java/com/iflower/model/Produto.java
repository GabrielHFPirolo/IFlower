package com.iflower.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;

/** Exclusão lógica (ativo=false) para preservar o histórico de pedidos. */
@Entity
@Table(name = "produto")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "loja_id", nullable = false)
    private Loja loja;

    @Column(nullable = false, length = 120)
    private String nome;

    @Column(length = 500)
    private String foto;

    @Column(length = 1000)
    private String descricao;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal preco;

    @Column(name = "permite_personalizacao", nullable = false)
    private boolean permitePersonalizacao;

    @Column(nullable = false)
    private boolean ativo = true;

    protected Produto() {
    }

    public Produto(Loja loja, String nome, String foto, String descricao, BigDecimal preco,
                   boolean permitePersonalizacao) {
        this.loja = loja;
        this.nome = nome;
        this.foto = foto;
        this.descricao = descricao;
        this.preco = preco;
        this.permitePersonalizacao = permitePersonalizacao;
    }

    public void atualizar(String nome, String foto, String descricao, BigDecimal preco,
                          boolean permitePersonalizacao) {
        this.nome = nome;
        this.foto = foto;
        this.descricao = descricao;
        this.preco = preco;
        this.permitePersonalizacao = permitePersonalizacao;
    }

    public void desativar() {
        this.ativo = false;
    }

    /** Compara por id; usa getId() para funcionar também com proxies do Hibernate. */
    public boolean mesmoProduto(Produto outro) {
        return this == outro || (getId() != null && getId().equals(outro.getId()));
    }

    public Long getId() { return id; }
    public Loja getLoja() { return loja; }
    public String getNome() { return nome; }
    public String getFoto() { return foto; }
    public String getDescricao() { return descricao; }
    public BigDecimal getPreco() { return preco; }
    public boolean isPermitePersonalizacao() { return permitePersonalizacao; }
    public boolean isAtivo() { return ativo; }
}
