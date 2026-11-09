package com.iflower.service;

import com.iflower.dto.LojaDTO;
import com.iflower.exception.RecursoNaoEncontradoException;
import com.iflower.exception.RegraNegocioException;
import com.iflower.model.Endereco;
import com.iflower.model.Loja;
import com.iflower.model.Usuario;
import com.iflower.repository.AvaliacaoRepository;
import com.iflower.repository.LojaRepository;
import com.iflower.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/** RF004 (lojas próximas), RF007 (busca por nome) e RF020 (perfil da loja). */
@Service
@Transactional
public class LojaService {

    private final LojaRepository lojaRepository;
    private final UsuarioRepository usuarioRepository;
    private final AvaliacaoRepository avaliacaoRepository;

    public LojaService(LojaRepository lojaRepository, UsuarioRepository usuarioRepository,
                       AvaliacaoRepository avaliacaoRepository) {
        this.lojaRepository = lojaRepository;
        this.usuarioRepository = usuarioRepository;
        this.avaliacaoRepository = avaliacaoRepository;
    }

    @Transactional(readOnly = true)
    public List<LojaDTO.Response> listar(String nome, String cidade, String uf) {
        return lojaRepository.buscar(vazioSeNulo(nome), vazioSeNulo(cidade), vazioSeNulo(uf))
                .stream().map(this::paraResponse).toList();
    }

    /**
     * Critério de proximidade adotado: lojas da mesma cidade/UF do endereço cadastrado do cliente.
     * (Evolução possível: raio em km por coordenadas.)
     */
    @Transactional(readOnly = true)
    public List<LojaDTO.Response> proximas(Long clienteId) {
        Usuario cliente = usuarioRepository.findById(clienteId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado."));
        Endereco endereco = cliente.getEndereco();
        if (endereco == null || endereco.getCidade() == null) {
            throw new RegraNegocioException("Cadastre seu endereço para ver lojas próximas.");
        }
        return listar(null, endereco.getCidade(), endereco.getUf());
    }

    @Transactional(readOnly = true)
    public LojaDTO.Response buscar(Long lojaId) {
        return paraResponse(buscarEntidade(lojaId));
    }

    @Transactional(readOnly = true)
    public LojaDTO.Response minhaLoja(Long vendedorId) {
        return paraResponse(buscarDoVendedor(vendedorId));
    }

    public LojaDTO.Response atualizarPerfil(Long vendedorId, LojaDTO.Atualizacao req) {
        Loja loja = buscarDoVendedor(vendedorId);
        loja.atualizarPerfil(req.nome().trim(), req.descricao(), req.endereco().toEntity(),
                req.abertura(), req.fechamento());
        return paraResponse(loja);
    }

    Loja buscarEntidade(Long lojaId) {
        return lojaRepository.findById(lojaId)
                .filter(Loja::isAtiva)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Loja não encontrada."));
    }

    Loja buscarDoVendedor(Long vendedorId) {
        return lojaRepository.findByVendedorId(vendedorId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Loja do vendedor não encontrada."));
    }

    private LojaDTO.Response paraResponse(Loja loja) {
        return LojaDTO.Response.from(loja, avaliacaoRepository.mediaPorLoja(loja.getId()),
                avaliacaoRepository.countByLojaId(loja.getId()));
    }

    private String vazioSeNulo(String valor) {
        return valor == null ? "" : valor.trim();
    }
}
