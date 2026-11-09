package com.iflower.service;

import com.iflower.dto.ProdutoDTO;
import com.iflower.exception.ProibidoException;
import com.iflower.exception.RecursoNaoEncontradoException;
import com.iflower.model.Loja;
import com.iflower.model.Produto;
import com.iflower.repository.ProdutoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/** RF008 e RF016 - cadastro e gerenciamento de produtos pelo vendedor. */
@Service
@Transactional
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final LojaService lojaService;

    public ProdutoService(ProdutoRepository produtoRepository, LojaService lojaService) {
        this.produtoRepository = produtoRepository;
        this.lojaService = lojaService;
    }

    public ProdutoDTO.Response criar(Long vendedorId, ProdutoDTO.Request req) {
        Loja loja = lojaService.buscarDoVendedor(vendedorId);
        Produto produto = new Produto(loja, req.nome().trim(), req.foto(), req.descricao(),
                req.preco(), req.permitePersonalizacao());
        return ProdutoDTO.Response.from(produtoRepository.save(produto));
    }

    public ProdutoDTO.Response atualizar(Long vendedorId, Long produtoId, ProdutoDTO.Request req) {
        Produto produto = buscarDoVendedor(vendedorId, produtoId);
        produto.atualizar(req.nome().trim(), req.foto(), req.descricao(), req.preco(), req.permitePersonalizacao());
        return ProdutoDTO.Response.from(produto);
    }

    public void excluir(Long vendedorId, Long produtoId) {
        buscarDoVendedor(vendedorId, produtoId).desativar();
    }

    @Transactional(readOnly = true)
    public ProdutoDTO.Response buscar(Long produtoId) {
        return ProdutoDTO.Response.from(produtoRepository.findById(produtoId)
                .filter(Produto::isAtivo)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Produto não encontrado.")));
    }

    @Transactional(readOnly = true)
    public List<ProdutoDTO.Response> listarPorLoja(Long lojaId) {
        lojaService.buscarEntidade(lojaId);
        return produtoRepository.findByLojaIdAndAtivoTrueOrderByNome(lojaId)
                .stream().map(ProdutoDTO.Response::from).toList();
    }

    @Transactional(readOnly = true)
    public List<ProdutoDTO.Response> listarDoVendedor(Long vendedorId) {
        Loja loja = lojaService.buscarDoVendedor(vendedorId);
        return listarPorLoja(loja.getId());
    }

    /** Garante que o produto existe, está ativo e pertence à loja do vendedor logado. */
    private Produto buscarDoVendedor(Long vendedorId, Long produtoId) {
        Loja loja = lojaService.buscarDoVendedor(vendedorId);
        Produto produto = produtoRepository.findById(produtoId)
                .filter(Produto::isAtivo)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Produto não encontrado."));
        if (!produto.getLoja().mesmaLoja(loja)) {
            throw new ProibidoException("Este produto pertence a outra loja.");
        }
        return produto;
    }
}
