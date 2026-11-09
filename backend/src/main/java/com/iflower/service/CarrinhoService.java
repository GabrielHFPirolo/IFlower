package com.iflower.service;

import com.iflower.dto.CarrinhoDTO;
import com.iflower.exception.RecursoNaoEncontradoException;
import com.iflower.model.Carrinho;
import com.iflower.model.Cliente;
import com.iflower.model.Produto;
import com.iflower.repository.CarrinhoRepository;
import com.iflower.repository.ClienteRepository;
import com.iflower.repository.ProdutoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** RF006 e RF018 - o carrinho é criado sob demanda, um por cliente. */
@Service
@Transactional
public class CarrinhoService {

    private final CarrinhoRepository carrinhoRepository;
    private final ClienteRepository clienteRepository;
    private final ProdutoRepository produtoRepository;

    public CarrinhoService(CarrinhoRepository carrinhoRepository, ClienteRepository clienteRepository,
                           ProdutoRepository produtoRepository) {
        this.carrinhoRepository = carrinhoRepository;
        this.clienteRepository = clienteRepository;
        this.produtoRepository = produtoRepository;
    }

    public CarrinhoDTO.Response obter(Long clienteId) {
        return CarrinhoDTO.Response.from(obterOuCriar(clienteId));
    }

    public CarrinhoDTO.Response adicionarItem(Long clienteId, CarrinhoDTO.AdicionarItem req) {
        Produto produto = produtoRepository.findById(req.produtoId())
                .filter(Produto::isAtivo)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Produto não encontrado."));
        Carrinho carrinho = obterOuCriar(clienteId);
        carrinho.adicionarItem(produto, req.quantidade(), req.personalizacao());
        return CarrinhoDTO.Response.from(carrinho);
    }

    public CarrinhoDTO.Response alterarQuantidade(Long clienteId, Long itemId, CarrinhoDTO.AlterarQuantidade req) {
        Carrinho carrinho = obterOuCriar(clienteId);
        carrinho.alterarQuantidade(itemId, req.quantidade());
        return CarrinhoDTO.Response.from(carrinho);
    }

    public CarrinhoDTO.Response removerItem(Long clienteId, Long itemId) {
        Carrinho carrinho = obterOuCriar(clienteId);
        carrinho.removerItem(itemId);
        return CarrinhoDTO.Response.from(carrinho);
    }

    public CarrinhoDTO.Response limpar(Long clienteId) {
        Carrinho carrinho = obterOuCriar(clienteId);
        carrinho.limpar();
        return CarrinhoDTO.Response.from(carrinho);
    }

    private Carrinho obterOuCriar(Long clienteId) {
        return carrinhoRepository.findByClienteId(clienteId).orElseGet(() -> {
            Cliente cliente = clienteRepository.findById(clienteId)
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente não encontrado."));
            return carrinhoRepository.save(new Carrinho(cliente));
        });
    }
}
