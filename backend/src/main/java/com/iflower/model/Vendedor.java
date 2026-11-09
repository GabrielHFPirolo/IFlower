package com.iflower.model;

import com.iflower.model.enums.Papel;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

/** Um vendedor possui exatamente uma loja (decisão de escopo; ver README). */
@Entity
@Table(name = "vendedor")
public class Vendedor extends Usuario {

    @OneToOne(mappedBy = "vendedor", cascade = CascadeType.ALL)
    private Loja loja;

    protected Vendedor() {
    }

    public Vendedor(String nome, String email, String senhaHash, String telefone, Endereco endereco) {
        super(nome, email, senhaHash, telefone, endereco);
    }

    public void vincularLoja(Loja loja) {
        this.loja = loja;
    }

    @Override
    public Papel getPapel() {
        return Papel.VENDEDOR;
    }

    public Loja getLoja() { return loja; }
}
