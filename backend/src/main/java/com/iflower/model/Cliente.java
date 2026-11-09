package com.iflower.model;

import com.iflower.model.enums.Papel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "cliente")
public class Cliente extends Usuario {

    @Column(nullable = false, unique = true, length = 11)
    private String cpf;

    protected Cliente() {
    }

    public Cliente(String nome, String email, String senhaHash, String telefone, Endereco endereco, String cpf) {
        super(nome, email, senhaHash, telefone, endereco);
        this.cpf = cpf;
    }

    @Override
    public Papel getPapel() {
        return Papel.CLIENTE;
    }

    public String getCpf() { return cpf; }
}
