package com.iflower.model;

import com.iflower.model.enums.TipoRetirada;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * SRP: a classe cuida somente do estado da própria entrega.
 * Não acessa banco, não altera pedido, não controla estoque.
 *
 * Evolução em relação à versão da atividade de SRP: virou entidade JPA, o vínculo
 * com o pedido passou de "pedidoId" para associação, e Date/String foram trocados
 * por LocalDate/LocalTime/TipoRetirada. selecionarEntrega() e cancelar() foram mantidos.
 */
@Entity
@Table(name = "entrega")
public class Entrega {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pedido_id", nullable = false, unique = true)
    private Pedido pedido;

    @Column(name = "data_entrega", nullable = false)
    private LocalDate dataEntrega;

    @Column(nullable = false)
    private LocalTime horario;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_retirada", nullable = false, length = 20)
    private TipoRetirada tipoRetirada;

    /** Endereço de destino; nulo quando o cliente retira na loja. */
    @Embedded
    private Endereco enderecoEntrega;

    @Column(nullable = false)
    private boolean selecionada;

    @Column(nullable = false)
    private boolean cancelada;

    protected Entrega() {
    }

    public Entrega(Pedido pedido, LocalDate dataEntrega, LocalTime horario,
                   TipoRetirada tipoRetirada, Endereco enderecoEntrega) {
        this.pedido = pedido;
        this.dataEntrega = dataEntrega;
        this.horario = horario;
        this.tipoRetirada = tipoRetirada;
        this.enderecoEntrega = enderecoEntrega;
        this.selecionada = false;
        this.cancelada = false;
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

    public Long getId() { return id; }
    public Pedido getPedido() { return pedido; }
    public LocalDate getDataEntrega() { return dataEntrega; }
    public LocalTime getHorario() { return horario; }
    public TipoRetirada getTipoRetirada() { return tipoRetirada; }
    public Endereco getEnderecoEntrega() { return enderecoEntrega; }
    public boolean isSelecionada() { return selecionada; }
    public boolean isCancelada() { return cancelada; }
}
