package com.lojamoda.service;

import com.lojamoda.exception.EntidadeNaoEncontradaException;
import com.lojamoda.model.Calcado;
import com.lojamoda.model.Produto;
import com.lojamoda.model.Roupa;
import com.lojamoda.persistence.ArquivoDAO;
import java.io.IOException;
import java.util.ArrayList;

public class ProdutoService {

    private static final String ARQUIVO_PRODUTOS = "dados/produtos.dat";

    private final ArrayList<Produto> produtos;
    private final ArquivoDAO<Produto> dao;

    public ProdutoService() {
        this.dao = new ArquivoDAO<>(ARQUIVO_PRODUTOS);
        this.produtos = carregarDoArquivo();
        ajustarContadorAposCarga();
    }

    private ArrayList<Produto> carregarDoArquivo() {
        try {
            return dao.carregar();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Aviso: não foi possível carregar produtos.dat (" + e.getMessage() + "). Iniciando lista vazia.");
            return new ArrayList<>();
        }
    }

    private void ajustarContadorAposCarga() {
        int maiorId = 0;
        for (Produto p : produtos) {
            if (p.getId() > maiorId) {
                maiorId = p.getId();
            }
        }
        Produto.ajustarContador(maiorId + 1);
    }

    public Roupa incluirRoupa(String nome, double preco, int qtdEstoque, String tamanho, String cor) {
        Roupa nova = new Roupa(nome, preco, qtdEstoque, tamanho, cor);
        produtos.add(nova);
        persistir();
        return nova;
    }

    public Calcado incluirCalcado(String nome, double preco, int qtdEstoque, int numeracao, String material) {
        Calcado novo = new Calcado(nome, preco, qtdEstoque, numeracao, material);
        produtos.add(novo);
        persistir();
        return novo;
    }

    public Produto consultar(int id) throws EntidadeNaoEncontradaException {
        for (Produto p : produtos) {
            if (p.getId() == id) {
                return p;
            }
        }
        throw new EntidadeNaoEncontradaException("Produto", id);
    }

    public void alterar(int id, String novoNome, double novoPreco, int novaQtd) throws EntidadeNaoEncontradaException {
        Produto produto = consultar(id);
        produto.setNome(novoNome);
        produto.setPreco(novoPreco);
        produto.setQtdEstoque(novaQtd);
        persistir();
    }

    public void excluir(int id) throws EntidadeNaoEncontradaException {
        Produto produto = consultar(id);
        produtos.remove(produto);
        persistir();
    }

    public ArrayList<Produto> listar() {
        return new ArrayList<>(produtos);
    }

    public void persistir() {
        try {
            dao.salvar(produtos);
        } catch (IOException e) {
            System.err.println("Erro ao salvar produtos.dat: " + e.getMessage());
        }
    }
}
