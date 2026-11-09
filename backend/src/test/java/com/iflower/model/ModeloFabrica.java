package com.iflower.model;

import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;

/** Monta objetos de domínio para os testes (atribui ids por reflexão, como o banco faria). */
final class ModeloFabrica {

    private ModeloFabrica() {
    }

    static Endereco endereco() {
        return new Endereco("80010000", "Rua A", "10", "Centro", "Curitiba", "PR");
    }

    static Cliente cliente(long id) {
        Cliente c = new Cliente("Maria", "maria@teste.com", "hash", "4199999", endereco(), "52998224725");
        ReflectionTestUtils.setField(c, "id", id);
        return c;
    }

    static Loja loja(long id) {
        Vendedor v = new Vendedor("Ana", "ana" + id + "@teste.com", "hash", "4199999", endereco());
        ReflectionTestUtils.setField(v, "id", 100 + id);
        Loja l = new Loja(v, "Loja " + id, "desc", "11222333000181", endereco(), null, null);
        ReflectionTestUtils.setField(l, "id", id);
        return l;
    }

    static Produto produto(long id, Loja loja, String preco, boolean personalizavel) {
        Produto p = new Produto(loja, "Produto " + id, null, null, new BigDecimal(preco), personalizavel);
        ReflectionTestUtils.setField(p, "id", id);
        return p;
    }
}
