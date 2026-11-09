package com.iflower.dto;

import com.iflower.model.Endereco;
import com.iflower.util.Documentos;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record EnderecoDTO(
        @NotBlank(message = "CEP é obrigatório")
        @Pattern(regexp = "\\d{5}-?\\d{3}", message = "CEP inválido") String cep,
        @NotBlank(message = "Logradouro é obrigatório") String logradouro,
        @NotBlank(message = "Número é obrigatório") String numero,
        String bairro,
        @NotBlank(message = "Cidade é obrigatória") String cidade,
        @NotBlank(message = "UF é obrigatória")
        @Pattern(regexp = "[A-Za-z]{2}", message = "UF deve ter 2 letras") String uf) {

    public Endereco toEntity() {
        return new Endereco(
                Documentos.somenteDigitos(cep),
                logradouro.trim(),
                numero.trim(),
                bairro == null ? null : bairro.trim(),
                cidade.trim(),
                uf.toUpperCase());
    }

    public static EnderecoDTO from(Endereco e) {
        if (e == null) {
            return null;
        }
        return new EnderecoDTO(e.getCep(), e.getLogradouro(), e.getNumero(), e.getBairro(), e.getCidade(), e.getUf());
    }
}
