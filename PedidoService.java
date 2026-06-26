package com.lojamoda.service;

import com.lojamoda.exception.EntidadeNaoEncontradaException;
import com.lojamoda.exception.EstoqueInsuficienteException;
import com.lojamoda.model.Cliente;
import com.lojamoda.model.Pedido;
import com.lojamoda.model.Produto;
import com.lojamoda.persistence.ArquivoDAO;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Camada de serviço responsável pelo registro de {@link Pedido}os de
 * venda, associando um {@link Cliente} a múltiplos {@link Produto}s e
 * dando baixa automática no estoque.
 *
 * <p>Mantém a lista de pedidos em memória e delega a persistência em
 * arquivo binário ao {@link ArquivoDAO}. Depende de {@link ProdutoService}
 * para validar e atualizar o estoque dos produtos vendidos.</p>
 *
 * @author Elisa Correia, Emilly Ranny, Jolie Pavan
 */
public class PedidoService {

    private static final String ARQUIVO_PEDIDOS = "dados/pedidos.dat";

    private final ArrayList<Pedido> pedidos;
    private final ArquivoDAO<Pedido> dao;
    private final ProdutoService produtoService;

    /**
     * Cria o serviço de pedidos, carregando automaticamente os dados já
     * persistidos em arquivo (se existentes) e ajustando o contador de
     * IDs para evitar colisões.
     *
     * @param produtoService serviço de produtos utilizado para validar e atualizar estoque
     */
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

    /**
     * Registra um novo pedido de venda para o cliente informado, contendo
     * os produtos indicados pelos seus IDs.
     *
     * <p>Antes de confirmar a venda, valida se cada produto possui
     * quantidade suficiente em estoque (regra de negócio). Caso algum
     * produto não tenha estoque suficiente, a operação é totalmente
     * cancelada (nenhum estoque é alterado) e a exceção é lançada.</p>
     *
     * @param cliente        cliente para o qual o pedido será registrado
     * @param idsProdutos    lista de IDs dos produtos a serem vendidos (podem repetir, indicando mais de uma unidade)
     * @return o pedido recém-criado, já com total calculado e estoque atualizado
     * @throws EntidadeNaoEncontradaException se algum ID de produto informado não existir
     * @throws EstoqueInsuficienteException   se algum produto não possuir estoque suficiente para a venda
     */
    public Pedido registrarVenda(Cliente cliente, ArrayList<Integer> idsProdutos)
            throws EntidadeNaoEncontradaException, EstoqueInsuficienteException {

        // 1ª etapa: resolve todos os produtos e valida estoque ANTES de alterar qualquer dado (operação atômica)
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

        // 2ª etapa: estoque validado para todos os itens, agora confirma a venda
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

    /**
     * Consulta um pedido pelo seu identificador único.
     *
     * @param id identificador do pedido
     * @return o pedido correspondente
     * @throws EntidadeNaoEncontradaException se nenhum pedido possuir o ID informado
     */
    public Pedido consultar(int id) throws EntidadeNaoEncontradaException {
        for (Pedido p : pedidos) {
            if (p.getIdPedido() == id) {
                return p;
            }
        }
        throw new EntidadeNaoEncontradaException("Pedido", id);
    }

    /**
     * Exclui um pedido (cancelamento de venda). O estoque dos produtos
     * envolvidos é devolvido automaticamente.
     *
     * @param id identificador do pedido a ser excluído
     * @throws EntidadeNaoEncontradaException se nenhum pedido possuir o ID informado
     */
    public void excluir(int id) throws EntidadeNaoEncontradaException {
        Pedido pedido = consultar(id);
        for (Produto produto : pedido.getProdutos()) {
            produto.setQtdEstoque(produto.getQtdEstoque() + 1);
        }
        pedidos.remove(pedido);
        persistir();
        produtoService.persistir();
    }

    /**
     * Lista todos os pedidos registrados.
     *
     * @return uma cópia da lista de pedidos registrados
     */
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
