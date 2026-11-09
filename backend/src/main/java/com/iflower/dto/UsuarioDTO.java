package com.iflower.dto;

import com.iflower.model.Cliente;
import com.iflower.model.Usuario;
import com.iflower.model.Vendedor;
import com.iflower.model.enums.Papel;
import com.iflower.validation.Cnpj;
import com.iflower.validation.Cpf;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalTime;

public final class UsuarioDTO {

    private UsuarioDTO() {
    }

    /** RF001 - cadastro de cliente. */
    public record ClienteCadastro(
            @NotBlank(message = "Nome é obrigatório") @Size(max = 120) String nome,
            @NotBlank(message = "Email é obrigatório") @Email(message = "Email inválido") String email,
            @NotBlank(message = "Senha é obrigatória")
            @Size(min = 8, max = 72, message = "A senha deve ter entre 8 e 72 caracteres") String senha,
            @NotBlank(message = "Telefone é obrigatório") @Size(max = 20) String telefone,
            @NotNull(message = "Endereço é obrigatório") @Valid EnderecoDTO endereco,
            @NotBlank(message = "CPF é obrigatório") @Cpf String cpf) {
    }

    /** RF001 (vendedor) - cadastro de vendedor já com o perfil da loja. */
    public record VendedorCadastro(
            @NotBlank(message = "Nome é obrigatório") @Size(max = 120) String nome,
            @NotBlank(message = "Email é obrigatório") @Email(message = "Email inválido") String email,
            @NotBlank(message = "Senha é obrigatória")
            @Size(min = 8, max = 72, message = "A senha deve ter entre 8 e 72 caracteres") String senha,
            @NotBlank(message = "Telefone é obrigatório") @Size(max = 20) String telefone,
            @NotNull(message = "Endereço é obrigatório") @Valid EnderecoDTO endereco,
            @NotBlank(message = "CNPJ é obrigatório") @Cnpj String cnpj,
            @NotBlank(message = "Nome da loja é obrigatório") @Size(max = 120) String nomeLoja,
            @Size(max = 500) String descricaoLoja,
            @Valid EnderecoDTO enderecoLoja,
            LocalTime abertura,
            LocalTime fechamento) {
    }

    /** RF005 - o usuário gerencia seus dados (email e documento não são alteráveis). */
    public record Atualizacao(
            @NotBlank(message = "Nome é obrigatório") @Size(max = 120) String nome,
            @NotBlank(message = "Telefone é obrigatório") @Size(max = 20) String telefone,
            @NotNull(message = "Endereço é obrigatório") @Valid EnderecoDTO endereco) {
    }

    public record Response(Long id, String nome, String email, String telefone, EnderecoDTO endereco,
                           Papel papel, String cpf, Long lojaId) {

        public static Response from(Usuario u) {
            String cpf = u instanceof Cliente c ? c.getCpf() : null;
            Long lojaId = (u instanceof Vendedor v && v.getLoja() != null) ? v.getLoja().getId() : null;
            return new Response(u.getId(), u.getNome(), u.getEmail(), u.getTelefone(),
                    EnderecoDTO.from(u.getEndereco()), u.getPapel(), cpf, lojaId);
        }
    }
}
