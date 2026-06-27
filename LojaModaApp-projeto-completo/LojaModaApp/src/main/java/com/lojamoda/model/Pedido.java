package com.lojamoda.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Representa um pedido de venda realizado na loja.
 *
 * <p>Associa um {@link Cliente} a uma lista de {@link Produto} vendidos,
 * registrando a data da venda e o valor total calculado.</p>
 *
 * <p>Relacionamentos: um Pedido possui um Cliente (associação 1:1) e
 * contém vários Produtos (agregação 1:N através de um {@link ArrayList}).</p>
 *
 * @author Elisa Correia, Emilly Ranny, Jolie Pavan
 */
public class Pedido implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Contador estático utilizado para gerar IDs sequenciais e únicos. */
    private static int contadorId = 1;

    private final int idPedido;
    private final Cliente cliente;
    private final ArrayList<Produto> produtos;
    private double valorTotal;
    private final LocalDate data;

    /**
     * Constrói um novo pedido associado a um cliente, atribuindo
     * automaticamente um ID único e sequencial e registrando a data atual.
     *
     * @param cliente cliente para o qual o pedido está sendo criado
     */
    public Pedido(Cliente cliente) {
        this.idPedido = contadorId++;
        this.cliente = cliente;
        this.produtos = new ArrayList<>();
        this.valorTotal = 0.0;
        this.data = LocalDate.now();
    }

    /**
     * Ajusta o contador estático de IDs para um valor específico.
     *
     * <p>Utilizado exclusivamente pela camada de persistência ao carregar
     * dados já existentes de arquivo, evitando colisão de IDs entre
     * pedidos carregados e novos pedidos criados na sessão atual.</p>
     *
     * @param valor próximo valor a ser utilizado pelo contador
     */
    public static void ajustarContador(int valor) {
        if (valor > contadorId) {
            contadorId = valor;
        }
    }

    /**
     * Adiciona um produto à lista de itens deste pedido e recalcula o
     * valor total automaticamente.
     *
     * @param p produto a ser adicionado ao pedido
     */
    public void adicionarProduto(Produto p) {
        this.produtos.add(p);
        calcularTotal();
    }

    /**
     * Calcula e atualiza o valor total do pedido somando o preço de
     * todos os produtos associados.
     *
     * @return o valor total atualizado do pedido
     */
    public double calcularTotal() {
        double total = 0.0;
        for (Produto p : produtos) {
            total += p.getPreco();
        }
        this.valorTotal = total;
        return this.valorTotal;
    }

    /** @return o identificador único do pedido */
    public int getIdPedido() {
        return idPedido;
    }

    /** @return o cliente associado a este pedido */
    public Cliente getCliente() {
        return cliente;
    }

    /** @return a lista de produtos contidos neste pedido */
    public ArrayList<Produto> getProdutos() {
        return produtos;
    }

    /** @return o valor total do pedido */
    public double getValorTotal() {
        return valorTotal;
    }

    /** @return a data em que o pedido foi registrado */
    public LocalDate getData() {
        return data;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Pedido #%d | Cliente: %s | Data: %s | Total: R$ %.2f%n",
                idPedido, cliente.getNome(), data, valorTotal));
        for (Produto p : produtos) {
            sb.append("   -> ").append(p.exibirDetalhes()).append(System.lineSeparator());
        }
        return sb.toString();
    }
}
