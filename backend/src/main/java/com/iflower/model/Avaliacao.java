package com.iflower.model;

import com.iflower.exception.RegraNegocioException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "avaliacao")
public class Avaliacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "loja_id", nullable = false)
    private Loja loja;

    @Column(nullable = false)
    private int nota;

    @Column(length = 500)
    private String comentario;

    @Column(name = "data_avaliacao", nullable = false)
    private LocalDateTime dataAvaliacao;

    protected Avaliacao() {
    }

    public Avaliacao(Cliente cliente, Loja loja, int nota, String comentario) {
        validarNota(nota);
        this.cliente = cliente;
        this.loja = loja;
        this.nota = nota;
        this.comentario = comentario;
        this.dataAvaliacao = LocalDateTime.now();
    }

    public void editar(int nota, String comentario) {
        validarNota(nota);
        this.nota = nota;
        this.comentario = comentario;
    }

    private static void validarNota(int nota) {
        if (nota < 1 || nota > 5) {
            throw new RegraNegocioException("A nota deve estar entre 1 e 5.");
        }
    }

    public Long getId() { return id; }
    public Cliente getCliente() { return cliente; }
    public Loja getLoja() { return loja; }
    public int getNota() { return nota; }
    public String getComentario() { return comentario; }
    public LocalDateTime getDataAvaliacao() { return dataAvaliacao; }
}
