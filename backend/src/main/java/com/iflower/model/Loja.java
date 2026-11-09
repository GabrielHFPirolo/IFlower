package com.iflower.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.time.LocalTime;

@Entity
@Table(name = "loja")
public class Loja {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "vendedor_id", nullable = false, unique = true)
    private Vendedor vendedor;

    @Column(nullable = false, length = 120)
    private String nome;

    @Column(length = 500)
    private String descricao;

    @Column(nullable = false, unique = true, length = 14)
    private String cnpj;

    @Embedded
    private Endereco endereco;

    private LocalTime abertura;
    private LocalTime fechamento;

    @Column(nullable = false)
    private boolean ativa = true;

    protected Loja() {
    }

    public Loja(Vendedor vendedor, String nome, String descricao, String cnpj, Endereco endereco,
                LocalTime abertura, LocalTime fechamento) {
        this.vendedor = vendedor;
        this.nome = nome;
        this.descricao = descricao;
        this.cnpj = cnpj;
        this.endereco = endereco;
        this.abertura = abertura;
        this.fechamento = fechamento;
    }

    public void atualizarPerfil(String nome, String descricao, Endereco endereco,
                                LocalTime abertura, LocalTime fechamento) {
        this.nome = nome;
        this.descricao = descricao;
        this.endereco = endereco;
        this.abertura = abertura;
        this.fechamento = fechamento;
    }

    /** Se a loja não definiu horário de funcionamento, qualquer horário é aceito. */
    public boolean aceitaHorario(LocalTime horario) {
        if (abertura == null || fechamento == null) {
            return true;
        }
        return !horario.isBefore(abertura) && !horario.isAfter(fechamento);
    }

    /** Compara por id; usa getId() para funcionar também com proxies do Hibernate. */
    public boolean mesmaLoja(Loja outra) {
        return this == outra || (getId() != null && getId().equals(outra.getId()));
    }

    public Long getId() { return id; }
    public Vendedor getVendedor() { return vendedor; }
    public String getNome() { return nome; }
    public String getDescricao() { return descricao; }
    public String getCnpj() { return cnpj; }
    public Endereco getEndereco() { return endereco; }
    public LocalTime getAbertura() { return abertura; }
    public LocalTime getFechamento() { return fechamento; }
    public boolean isAtiva() { return ativa; }
}
