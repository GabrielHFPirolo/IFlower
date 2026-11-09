package com.iflower.model;

import com.iflower.model.enums.Papel;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;

/**
 * Generalização de Cliente e Vendedor (herança JOINED: tabela usuario + tabelas filhas).
 * A entidade guarda dados e regras do próprio objeto; cadastro, login e persistência
 * ficam em UsuarioService/AuthService (SRP).
 */
@Entity
@Table(name = "usuario")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String nome;

    @Column(nullable = false, unique = true, length = 160)
    private String email;

    @Column(name = "senha_hash", nullable = false)
    private String senhaHash;

    @Column(length = 20)
    private String telefone;

    @Embedded
    private Endereco endereco;

    @Column(nullable = false)
    private boolean ativo = true;

    protected Usuario() {
    }

    protected Usuario(String nome, String email, String senhaHash, String telefone, Endereco endereco) {
        this.nome = nome;
        this.email = email;
        this.senhaHash = senhaHash;
        this.telefone = telefone;
        this.endereco = endereco;
    }

    public abstract Papel getPapel();

    public void atualizarDados(String nome, String telefone, Endereco endereco) {
        this.nome = nome;
        this.telefone = telefone;
        this.endereco = endereco;
    }

    public void desativar() {
        this.ativo = false;
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getEmail() { return email; }
    public String getSenhaHash() { return senhaHash; }
    public String getTelefone() { return telefone; }
    public Endereco getEndereco() { return endereco; }
    public boolean isAtivo() { return ativo; }
}
