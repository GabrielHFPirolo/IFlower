package com.iflower.model;

import com.iflower.exception.RegraNegocioException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CarrinhoTest {

    private final Loja lojaA = ModeloFabrica.loja(1);
    private final Loja lojaB = ModeloFabrica.loja(2);
    private final Carrinho carrinho = new Carrinho(ModeloFabrica.cliente(1));

    @Test
    void somaQuantidadeDoMesmoProdutoEMantemUmaLinha() {
        Produto rosa = ModeloFabrica.produto(1, lojaA, "10.00", false);
        carrinho.adicionarItem(rosa, 2, null);
        carrinho.adicionarItem(rosa, 3, null);

        assertEquals(1, carrinho.getItens().size());
        assertEquals(5, carrinho.getItens().get(0).getQuantidade());
        assertEquals(new BigDecimal("50.00"), carrinho.calcularTotal());
    }

    @Test
    void personalizacoesDiferentesGeramLinhasDiferentes() {
        Produto buque = ModeloFabrica.produto(1, lojaA, "100.00", true);
        carrinho.adicionarItem(buque, 1, "Para Maria");
        carrinho.adicionarItem(buque, 1, "Para Ana");
        assertEquals(2, carrinho.getItens().size());
    }

    @Test
    void naoMisturaProdutosDeLojasDiferentes() {
        carrinho.adicionarItem(ModeloFabrica.produto(1, lojaA, "10.00", false), 1, null);
        Produto deOutraLoja = ModeloFabrica.produto(2, lojaB, "20.00", false);
        assertThrows(RegraNegocioException.class, () -> carrinho.adicionarItem(deOutraLoja, 1, null));
    }

    @Test
    void rejeitaPersonalizacaoEmProdutoQueNaoPermite() {
        Produto simples = ModeloFabrica.produto(1, lojaA, "10.00", false);
        assertThrows(RegraNegocioException.class, () -> carrinho.adicionarItem(simples, 1, "texto"));
    }

    @Test
    void rejeitaQuantidadeInvalidaEProdutoInativo() {
        Produto p = ModeloFabrica.produto(1, lojaA, "10.00", false);
        assertThrows(RegraNegocioException.class, () -> carrinho.adicionarItem(p, 0, null));
        p.desativar();
        assertThrows(RegraNegocioException.class, () -> carrinho.adicionarItem(p, 1, null));
    }

    @Test
    void limparLiberaALojaDoCarrinho() {
        carrinho.adicionarItem(ModeloFabrica.produto(1, lojaA, "10.00", false), 1, null);
        carrinho.limpar();
        assertNull(carrinho.getLoja());
        // agora pode comprar de outra loja
        carrinho.adicionarItem(ModeloFabrica.produto(2, lojaB, "20.00", false), 1, null);
        assertEquals(1, carrinho.getItens().size());
    }
}
