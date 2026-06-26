package com.lojamoda.model;

import java.io.Serializable;

/**
 * Classe abstrata que representa um produto genérico do catálogo da loja.
 *
 * <p>É a classe base para todas as especializações de produto (como
 * {@link Roupa}), concentrando os atributos e comportamentos comuns:
 * identificador único, nome, preço e quantidade em estoque.</p>
 *
 * <p>O identificador ({@code id}) é gerado automaticamente e de forma
 * sequencial através do atributo estático {@code contadorId}, garantindo
 * que cada produto criado durante a execução da aplicação receba um
 * código único.</p>
 *
 * @author Elisa Correia, Emilly Ranny, Jolie Pavan
 */
public abstract class Produto implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Contador estático utilizado para gerar IDs sequenciais e únicos. */
    private static int contadorId = 1;

    private final int id;
    private String nome;
    private double preco;
    private int qtdEstoque;

    /**
     * Constrói um novo produto, atribuindo automaticamente um ID único e sequencial.
     *
     * @param nome       nome do produto
     * @param preco      preço de venda do produto (deve ser maior ou igual a zero)
     * @param qtdEstoque quantidade inicial em estoque (deve ser maior ou igual a zero)
     */
    protected Produto(String nome, double preco, int qtdEstoque) {
        this.id = contadorId++;
        this.nome = nome;
        this.preco = preco;
        this.qtdEstoque = qtdEstoque;
    }

    /**
     * Ajusta o contador estático de IDs para um valor específico.
     *
     * <p>Utilizado exclusivamente pela camada de persistência ao carregar
     * dados já existentes de arquivo, evitando colisão de IDs entre
     * produtos carregados e novos produtos criados na sessão atual.</p>
     *
     * @param valor próximo valor a ser utilizado pelo contador
     */
    public static void ajustarContador(int valor) {
        if (valor > contadorId) {
            contadorId = valor;
        }
    }

    /**
     * Exibe (retorna como texto) os detalhes específicos do produto.
     * Cada subclasse deve implementar este método de acordo com seus
     * atributos próprios (polimorfismo).
     *
     * @return uma {@code String} formatada com os detalhes do produto
     */
    public abstract String exibirDetalhes();

    /** @return o identificador único do produto */
    public int getId() {
        return id;
    }

    /** @return o nome do produto */
    public String getNome() {
        return nome;
    }

    /** @param nome novo nome do produto */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /** @return o preço de venda do produto */
    public double getPreco() {
        return preco;
    }

    /** @param preco novo preço de venda do produto */
    public void setPreco(double preco) {
        this.preco = preco;
    }

    /** @return a quantidade atual em estoque */
    public int getQtdEstoque() {
        return qtdEstoque;
    }

    /** @param qtdEstoque nova quantidade em estoque */
    public void setQtdEstoque(int qtdEstoque) {
        this.qtdEstoque = qtdEstoque;
    }

    @Override
    public String toString() {
    return "[ID: " + this.id + "] " + this.nome + " - R$ " + String.format("%.2f", this.preco) + " (Estoque: " + this.qtdEstoque + ")";
    }
}
