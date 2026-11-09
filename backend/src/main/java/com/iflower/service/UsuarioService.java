package com.iflower.service;

import com.iflower.dto.UsuarioDTO;
import com.iflower.exception.ConflitoException;
import com.iflower.exception.RecursoNaoEncontradoException;
import com.iflower.model.Cliente;
import com.iflower.model.Endereco;
import com.iflower.model.Loja;
import com.iflower.model.Usuario;
import com.iflower.model.Vendedor;
import com.iflower.repository.ClienteRepository;
import com.iflower.repository.LojaRepository;
import com.iflower.repository.UsuarioRepository;
import com.iflower.repository.VendedorRepository;
import com.iflower.util.Documentos;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** RF001 (cadastro de cliente e vendedor) e RF005 (gerenciar dados). */
@Service
@Transactional
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final ClienteRepository clienteRepository;
    private final VendedorRepository vendedorRepository;
    private final LojaRepository lojaRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, ClienteRepository clienteRepository,
                          VendedorRepository vendedorRepository, LojaRepository lojaRepository,
                          PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.clienteRepository = clienteRepository;
        this.vendedorRepository = vendedorRepository;
        this.lojaRepository = lojaRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UsuarioDTO.Response cadastrarCliente(UsuarioDTO.ClienteCadastro req) {
        String email = normalizarEmail(req.email());
        String cpf = Documentos.somenteDigitos(req.cpf());
        validarEmailLivre(email);
        if (clienteRepository.existsByCpf(cpf)) {
            throw new ConflitoException("CPF já cadastrado.");
        }
        Cliente cliente = new Cliente(req.nome().trim(), email, passwordEncoder.encode(req.senha()),
                req.telefone().trim(), req.endereco().toEntity(), cpf);
        return UsuarioDTO.Response.from(clienteRepository.save(cliente));
    }

    public UsuarioDTO.Response cadastrarVendedor(UsuarioDTO.VendedorCadastro req) {
        String email = normalizarEmail(req.email());
        String cnpj = Documentos.somenteDigitos(req.cnpj());
        validarEmailLivre(email);
        if (lojaRepository.existsByCnpj(cnpj)) {
            throw new ConflitoException("CNPJ já cadastrado.");
        }
        Endereco enderecoVendedor = req.endereco().toEntity();
        Endereco enderecoLoja = req.enderecoLoja() != null ? req.enderecoLoja().toEntity() : enderecoVendedor;

        Vendedor vendedor = new Vendedor(req.nome().trim(), email, passwordEncoder.encode(req.senha()),
                req.telefone().trim(), enderecoVendedor);
        vendedor.vincularLoja(new Loja(vendedor, req.nomeLoja().trim(), req.descricaoLoja(), cnpj,
                enderecoLoja, req.abertura(), req.fechamento()));
        return UsuarioDTO.Response.from(vendedorRepository.save(vendedor));
    }

    @Transactional(readOnly = true)
    public UsuarioDTO.Response buscarPerfil(Long usuarioId) {
        return UsuarioDTO.Response.from(buscar(usuarioId));
    }

    public UsuarioDTO.Response atualizarPerfil(Long usuarioId, UsuarioDTO.Atualizacao req) {
        Usuario usuario = buscar(usuarioId);
        usuario.atualizarDados(req.nome().trim(), req.telefone().trim(), req.endereco().toEntity());
        return UsuarioDTO.Response.from(usuario);
    }

    /** Exclusão lógica: preserva pedidos e avaliações já registrados. */
    public void desativar(Long usuarioId) {
        buscar(usuarioId).desativar();
    }

    private Usuario buscar(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado."));
    }

    private void validarEmailLivre(String email) {
        if (usuarioRepository.existsByEmailIgnoreCase(email)) {
            throw new ConflitoException("Email já cadastrado.");
        }
    }

    private String normalizarEmail(String email) {
        return email.trim().toLowerCase();
    }
}
