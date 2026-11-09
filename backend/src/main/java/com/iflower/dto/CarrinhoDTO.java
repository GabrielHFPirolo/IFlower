package com.iflower.dto;

import com.iflower.model.Carrinho;
import com.iflower.model.ItemCarrinho;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.List;

public final class CarrinhoDTO {

    private CarrinhoDTO() {
    }

    public record AdicionarItem(
            @NotNull(message = "produtoId é obrigatório") Long produtoId,
            @Min(value = 1, message = "A quantidade deve ser de pelo menos 1") int quantidade,
            @Size(max = 300) String personalizacao) {
    }

    public record AlterarQuantidade(
            @Min(value = 1, message = "A quantidade deve ser de pelo menos 1") int quantidade) {
    }

    public record ItemResponse(Long id, Long produtoId, String produtoNome, BigDecimal precoUnitario,
                               int quantidade, String personalizacao, BigDecimal subtotal) {

        public static ItemResponse from(ItemCarrinho i) {
            return new ItemResponse(i.getId(), i.getProduto().getId(), i.getProduto().getNome(),
                    i.getProduto().getPreco(), i.getQuantidade(), i.getPersonalizacao(), i.subtotal());
        }
    }

    public record Response(Long id, Long lojaId, String lojaNome, List<ItemResponse> itens, BigDecimal total) {

        public static Response from(Carrinho c) {
            Long lojaId = c.getLoja() == null ? null : c.getLoja().getId();
            String lojaNome = c.getLoja() == null ? null : c.getLoja().getNome();
            return new Response(c.getId(), lojaId, lojaNome,
                    c.getItens().stream().map(ItemResponse::from).toList(), c.calcularTotal());
        }
    }
}
