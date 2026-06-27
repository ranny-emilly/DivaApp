package com.lojamoda.service;

import com.lojamoda.exception.EntidadeNaoEncontradaException;
import com.lojamoda.exception.EstoqueInsuficienteException;
import com.lojamoda.model.Cliente;
import com.lojamoda.model.Pedido;
import com.lojamoda.model.Produto;
import com.lojamoda.persistence.ArquivoDAO;

import java.io.IOException;
import java.util.ArrayList;


public class PedidoService {

    private static final String ARQUIVO_PEDIDOS = "dados/pedidos.dat";

    private final ArrayList<Pedido> pedidos;
    private final ArquivoDAO<Pedido> dao;
    private final ProdutoService produtoService;

    public PedidoService(ProdutoService produtoService) {
        this.produtoService = produtoService;
        this.dao = new ArquivoDAO<>(ARQUIVO_PEDIDOS);
        this.pedidos = carregarDoArquivo();
        ajustarContadorAposCarga();
    }

    private ArrayList<Pedido> carregarDoArquivo() {
        try {
            return dao.carregar();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Aviso: não foi possível carregar pedidos.dat (" + e.getMessage() + "). Iniciando lista vazia.");
            return new ArrayList<>();
        }
    }

    private void ajustarContadorAposCarga() {
        int maiorId = 0;
        for (Pedido p : pedidos) {
            if (p.getIdPedido() > maiorId) {
                maiorId = p.getIdPedido();
            }
        }
        Pedido.ajustarContador(maiorId + 1);
    }

    public Pedido registrarVenda(Cliente cliente, ArrayList<Integer> idsProdutos)
            throws EntidadeNaoEncontradaException, EstoqueInsuficienteException {

        ArrayList<Produto> produtosDoPedido = new ArrayList<>();
        java.util.Map<Integer, Integer> quantidadePorId = new java.util.HashMap<>();

        for (Integer idProduto : idsProdutos) {
            Produto produto = produtoService.consultar(idProduto);
            produtosDoPedido.add(produto);
            quantidadePorId.merge(idProduto, 1, Integer::sum);
        }

        for (java.util.Map.Entry<Integer, Integer> entrada : quantidadePorId.entrySet()) {
            Produto produto = produtoService.consultar(entrada.getKey());
            int quantidadeSolicitada = entrada.getValue();
            if (produto.getQtdEstoque() < quantidadeSolicitada) {
                throw new EstoqueInsuficienteException(produto.getNome(), quantidadeSolicitada, produto.getQtdEstoque());
            }
        }

        Pedido novoPedido = new Pedido(cliente);
        for (Produto produto : produtosDoPedido) {
            produto.setQtdEstoque(produto.getQtdEstoque() - 1);
            novoPedido.adicionarProduto(produto);
        }

        pedidos.add(novoPedido);
        persistir();
        produtoService.persistir();

        return novoPedido;
    }

    public Pedido consultar(int id) throws EntidadeNaoEncontradaException {
        for (Pedido p : pedidos) {
            if (p.getIdPedido() == id) {
                return p;
            }
        }
        throw new EntidadeNaoEncontradaException("Pedido", id);
    }

    public void excluir(int id) throws EntidadeNaoEncontradaException {
        Pedido pedido = consultar(id);
        for (Produto produto : pedido.getProdutos()) {
            produto.setQtdEstoque(produto.getQtdEstoque() + 1);
        }
        pedidos.remove(pedido);
        persistir();
        produtoService.persistir();
    }

    public ArrayList<Pedido> listar() {
        return new ArrayList<>(pedidos);
    }

    private void persistir() {
        try {
            dao.salvar(pedidos);
        } catch (IOException e) {
            System.err.println("Erro ao salvar pedidos.dat: " + e.getMessage());
        }
    }
}
