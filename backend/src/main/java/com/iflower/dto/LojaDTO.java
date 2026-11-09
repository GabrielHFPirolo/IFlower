package com.iflower.dto;

import com.iflower.model.Loja;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalTime;

public final class LojaDTO {

    private LojaDTO() {
    }

    /** RF020 - perfil da loja. */
    public record Atualizacao(
            @NotBlank(message = "Nome da loja é obrigatório") @Size(max = 120) String nome,
            @Size(max = 500) String descricao,
            @NotNull(message = "Endereço é obrigatório") @Valid EnderecoDTO endereco,
            LocalTime abertura,
            LocalTime fechamento) {
    }

    public record Response(Long id, String nome, String descricao, String cnpj, EnderecoDTO endereco,
                           LocalTime abertura, LocalTime fechamento, Double notaMedia, long totalAvaliacoes) {

        public static Response from(Loja l, Double notaMedia, long totalAvaliacoes) {
            Double arredondada = notaMedia == null ? null : Math.round(notaMedia * 10.0) / 10.0;
            return new Response(l.getId(), l.getNome(), l.getDescricao(), l.getCnpj(),
                    EnderecoDTO.from(l.getEndereco()), l.getAbertura(), l.getFechamento(),
                    arredondada, totalAvaliacoes);
        }
    }
}
