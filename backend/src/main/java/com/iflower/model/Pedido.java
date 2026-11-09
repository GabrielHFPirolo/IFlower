package com.iflower.model;

import com.iflower.exception.RegraNegocioException;
import com.iflower.model.enums.StatusPedido;
import com.iflower.model.enums.TipoRetirada;
import com.iflower.model.pagamento.PagamentoBase;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pedido")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "loja_id", nullable = false)
    private Loja loja;

    @Column(name = "data_pedido", nullable = false)
    private LocalDateTime dataPedido;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private StatusPedido status;

    @Column(name = "valor_total", nullable = false, precision = 12, scale = 2)
    private BigDecimal valorTotal = BigDecimal.ZERO;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
    private List<ItemPedido> itens = new ArrayList<>();

    @OneToOne(mappedBy = "pedido", cascade = CascadeType.ALL)
    private Entrega entrega;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
    private List<PagamentoBase> pagamentos = new ArrayList<>();

    protected Pedido() {
    }

    public static Pedido novo(Cliente cliente, Loja loja) {
        Pedido p = new Pedido();
        p.cliente = cliente;
        p.loja = loja;
        p.dataPedido = LocalDateTime.now();
        p.status = StatusPedido.AGUARDANDO_PAGAMENTO;
        return p;
    }

    public void adicionarItem(ItemPedido item) {
        item.vincular(this);
        itens.add(item);
        this.valorTotal = calcularTotal();
    }

    public BigDecimal calcularTotal() {
        return itens.stream().map(ItemPedido::subtotal).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void definirEntrega(Entrega entrega) {
        this.entrega = entrega;
    }

    public void registrarPagamento(PagamentoBase pagamento) {
        pagamentos.add(pagamento);
    }

    /** Chamado quando um pagamento é aprovado. */
    public void confirmarPagamento() {
        atualizarStatus(StatusPedido.CONFIRMADO);
    }

    public void cancelar() {
        atualizarStatus(StatusPedido.CANCELADO);
    }

    public void atualizarStatus(StatusPedido novo) {
        if (!status.podeIrPara(novo)) {
            throw new RegraNegocioException("Transição de status inválida: " + status + " -> " + novo + ".");
        }
        TipoRetirada tipo = entrega != null ? entrega.getTipoRetirada() : null;
        if (novo == StatusPedido.SAIU_PARA_ENTREGA && tipo != TipoRetirada.ENTREGA) {
            throw new RegraNegocioException("Pedido para retirada na loja não pode sair para entrega.");
        }
        if (novo == StatusPedido.PRONTO_PARA_RETIRADA && tipo != TipoRetirada.RETIRADA_NA_LOJA) {
            throw new RegraNegocioException("Pedido com entrega não pode ficar pronto para retirada.");
        }
        if (novo == StatusPedido.CANCELADO && entrega != null && !entrega.isCancelada()) {
            entrega.cancelar();
        }
        this.status = novo;
    }

    public Long getId() { return id; }
    public Cliente getCliente() { return cliente; }
    public Loja getLoja() { return loja; }
    public LocalDateTime getDataPedido() { return dataPedido; }
    public StatusPedido getStatus() { return status; }
    public BigDecimal getValorTotal() { return valorTotal; }
    public List<ItemPedido> getItens() { return itens; }
    public Entrega getEntrega() { return entrega; }
    public List<PagamentoBase> getPagamentos() { return pagamentos; }
}
