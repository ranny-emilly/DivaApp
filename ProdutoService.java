package com.lojamoda.service;

import com.lojamoda.exception.EntidadeNaoEncontradaException;
import com.lojamoda.model.Calcado;
import com.lojamoda.model.Produto;
import com.lojamoda.model.Roupa;
import com.lojamoda.persistence.ArquivoDAO;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Camada de serviço responsável pelas regras de negócio e operações de
 * CRUD (Inclusão, Exclusão, Alteração, Consulta e Listagem) sobre
 * {@link Produto} (incluindo suas especializações {@link Roupa} e
 * {@link Calcado}).
 *
 * <p>Mantém a lista de produtos em memória e delega a persistência em
 * arquivo binário ao {@link ArquivoDAO}.</p>
 *
 * @author Elisa Correia, Emilly Ranny, Jolie Pavan
 */
public class ProdutoService {

    private static final String ARQUIVO_PRODUTOS = "dados/produtos.dat";

    private final ArrayList<Produto> produtos;
    private final ArquivoDAO<Produto> dao;

    /**
     * Cria o serviço de produtos, carregando automaticamente os dados
     * já persistidos em arquivo (se existentes) e ajustando o contador
     * de IDs para evitar colisões.
     */
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

    /**
     * Inclui uma nova roupa no catálogo.
     *
     * @param nome       nome da roupa
     * @param preco      preço de venda (deve ser maior ou igual a zero)
     * @param qtdEstoque quantidade inicial em estoque (deve ser maior ou igual a zero)
     * @param tamanho    tamanho da peça (ex: P, M, G)
     * @param cor        cor da peça
     * @return a roupa recém-criada, já com seu ID gerado automaticamente
     */
    public Roupa incluirRoupa(String nome, double preco, int qtdEstoque, String tamanho, String cor) {
        Roupa nova = new Roupa(nome, preco, qtdEstoque, tamanho, cor);
        produtos.add(nova);
        persistir();
        return nova;
    }

    /**
     * Inclui um novo calçado no catálogo.
     *
     * @param nome       nome do calçado
     * @param preco      preço de venda (deve ser maior ou igual a zero)
     * @param qtdEstoque quantidade inicial em estoque (deve ser maior ou igual a zero)
     * @param numeracao  numeração do calçado
     * @param material   material de fabricação
     * @return o calçado recém-criado, já com seu ID gerado automaticamente
     */
    public Calcado incluirCalcado(String nome, double preco, int qtdEstoque, int numeracao, String material) {
        Calcado novo = new Calcado(nome, preco, qtdEstoque, numeracao, material);
        produtos.add(novo);
        persistir();
        return novo;
    }

    /**
     * Consulta um produto pelo seu identificador único.
     *
     * @param id identificador do produto
     * @return o produto correspondente
     * @throws EntidadeNaoEncontradaException se nenhum produto possuir o ID informado
     */
    public Produto consultar(int id) throws EntidadeNaoEncontradaException {
        for (Produto p : produtos) {
            if (p.getId() == id) {
                return p;
            }
        }
        throw new EntidadeNaoEncontradaException("Produto", id);
    }

    /**
     * Altera o preço e a quantidade em estoque de um produto já existente.
     *
     * @param id            identificador do produto a ser alterado
     * @param novoNome      novo nome do produto
     * @param novoPreco     novo preço de venda
     * @param novaQtd       nova quantidade em estoque
     * @throws EntidadeNaoEncontradaException se nenhum produto possuir o ID informado
     */
    public void alterar(int id, String novoNome, double novoPreco, int novaQtd) throws EntidadeNaoEncontradaException {
        Produto produto = consultar(id);
        produto.setNome(novoNome);
        produto.setPreco(novoPreco);
        produto.setQtdEstoque(novaQtd);
        persistir();
    }

    /**
     * Exclui um produto do catálogo.
     *
     * @param id identificador do produto a ser excluído
     * @throws EntidadeNaoEncontradaException se nenhum produto possuir o ID informado
     */
    public void excluir(int id) throws EntidadeNaoEncontradaException {
        Produto produto = consultar(id);
        produtos.remove(produto);
        persistir();
    }

    /**
     * Lista todos os produtos cadastrados (roupas e calçados).
     *
     * @return uma cópia da lista de produtos cadastrados
     */
    public ArrayList<Produto> listar() {
        return new ArrayList<>(produtos);
    }

    /**
     * Persiste o estado atual da lista de produtos em arquivo.
     * Exposto para que outros serviços (como {@link PedidoService}) possam
     * salvar a baixa de estoque após uma venda.
     */
    public void persistir() {
        try {
            dao.salvar(produtos);
        } catch (IOException e) {
            System.err.println("Erro ao salvar produtos.dat: " + e.getMessage());
        }
    }
}
