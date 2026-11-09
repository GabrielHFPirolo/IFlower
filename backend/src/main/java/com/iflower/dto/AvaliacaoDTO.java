package com.iflower.dto;

import com.iflower.model.Avaliacao;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public final class AvaliacaoDTO {

    private AvaliacaoDTO() {
    }

    public record Request(
            @Min(value = 1, message = "A nota deve estar entre 1 e 5")
            @Max(value = 5, message = "A nota deve estar entre 1 e 5") int nota,
            @Size(max = 500) String comentario) {
    }

    public record Response(Long id, Long lojaId, Long clienteId, String clienteNome, int nota,
                           String comentario, LocalDateTime data) {

        public static Response from(Avaliacao a) {
            return new Response(a.getId(), a.getLoja().getId(), a.getCliente().getId(),
                    a.getCliente().getNome(), a.getNota(), a.getComentario(), a.getDataAvaliacao());
        }
    }
}
