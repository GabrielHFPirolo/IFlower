package com.iflower.dto;

import com.iflower.model.Produto;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public final class ProdutoDTO {

    private ProdutoDTO() {
    }

    /** RF008 - nome, foto, descrição, preço e personalização. */
    public record Request(
            @NotBlank(message = "Nome é obrigatório") @Size(max = 120) String nome,
            @Size(max = 500) String foto,
            @Size(max = 1000) String descricao,
            @NotNull(message = "Preço é obrigatório")
            @DecimalMin(value = "0.01", message = "O preço deve ser maior que zero") BigDecimal preco,
            boolean permitePersonalizacao) {
    }

    public record Response(Long id, Long lojaId, String nome, String foto, String descricao,
                           BigDecimal preco, boolean permitePersonalizacao) {

        public static Response from(Produto p) {
            return new Response(p.getId(), p.getLoja().getId(), p.getNome(), p.getFoto(),
                    p.getDescricao(), p.getPreco(), p.isPermitePersonalizacao());
        }
    }
}
