package com.iflower.model;

import com.iflower.exception.RecursoNaoEncontradoException;
import com.iflower.exception.RegraNegocioException;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Regra de negócio: o carrinho só aceita produtos de uma única loja,
 * porque cada pedido pertence a uma loja.
 */
@Entity
@Table(name = "carrinho")
public class Carrinho {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cliente_id", nullable = false, unique = true)
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loja_id")
    private Loja loja;

    @OneToMany(mappedBy = "carrinho", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemCarrinho> itens = new ArrayList<>();

    protected Carrinho() {
    }

    public Carrinho(Cliente cliente) {
        this.cliente = cliente;
    }

    public void adicionarItem(Produto produto, int quantidade, String personalizacao) {
        if (quantidade < 1) {
            throw new RegraNegocioException("A quantidade deve ser de pelo menos 1.");
        }
        if (!produto.isAtivo()) {
            throw new RegraNegocioException("O produto não está mais disponível.");
        }
        if (personalizacao != null && !personalizacao.isBlank() && !produto.isPermitePersonalizacao()) {
            throw new RegraNegocioException("Este produto não permite personalização.");
        }
        if (!itens.isEmpty() && !loja.mesmaLoja(produto.getLoja())) {
            throw new RegraNegocioException(
                    "O carrinho só pode conter produtos de uma única loja. Esvazie o carrinho para comprar em outra loja.");
        }
        String obs = personalizacao == null || personalizacao.isBlank() ? null : personalizacao.trim();
        for (ItemCarrinho item : itens) {
            if (item.getProduto().mesmoProduto(produto) && Objects.equals(item.getPersonalizacao(), obs)) {
                item.somarQuantidade(quantidade);
                return;
            }
        }
        this.loja = produto.getLoja();
        itens.add(new ItemCarrinho(this, produto, quantidade, obs));
    }

    public void alterarQuantidade(Long itemId, int quantidade) {
        if (quantidade < 1) {
            throw new RegraNegocioException("A quantidade deve ser de pelo menos 1. Para excluir, remova o item.");
        }
        buscarItem(itemId).definirQuantidade(quantidade);
    }

    public void removerItem(Long itemId) {
        itens.remove(buscarItem(itemId));
        if (itens.isEmpty()) {
            loja = null;
        }
    }

    public void limpar() {
        itens.clear();
        loja = null;
    }

    public BigDecimal calcularTotal() {
        return itens.stream().map(ItemCarrinho::subtotal).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private ItemCarrinho buscarItem(Long itemId) {
        return itens.stream()
                .filter(i -> Objects.equals(i.getId(), itemId))
                .findFirst()
                .orElseThrow(() -> new RecursoNaoEncontradoException("Item não encontrado no carrinho."));
    }

    public Long getId() { return id; }
    public Cliente getCliente() { return cliente; }
    public Loja getLoja() { return loja; }
    public List<ItemCarrinho> getItens() { return itens; }
}
