package com.iflower.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

/** Objeto de valor reutilizado por Usuario, Loja e Entrega. */
@Embeddable
public class Endereco {

    @Column(length = 8)
    private String cep;
    @Column(length = 160)
    private String logradouro;
    @Column(length = 20)
    private String numero;
    @Column(length = 80)
    private String bairro;
    @Column(length = 80)
    private String cidade;
    @Column(length = 2)
    private String uf;

    protected Endereco() {
    }

    public Endereco(String cep, String logradouro, String numero, String bairro, String cidade, String uf) {
        this.cep = cep;
        this.logradouro = logradouro;
        this.numero = numero;
        this.bairro = bairro;
        this.cidade = cidade;
        this.uf = uf;
    }

    public String getCep() { return cep; }
    public String getLogradouro() { return logradouro; }
    public String getNumero() { return numero; }
    public String getBairro() { return bairro; }
    public String getCidade() { return cidade; }
    public String getUf() { return uf; }
}
