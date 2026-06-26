package com.lojamoda.model;

/**
 * Representa uma peça de roupa do catálogo da loja.
 *
 * <p>É uma especialização (herança) de {@link Produto}, adicionando os
 * atributos específicos do setor de moda: tamanho e cor.</p>
 *
 * @author Elisa Correia, Emilly Ranny, Jolie Pavan, Thyago Divino
 */
public class Roupa extends Produto {

    private static final long serialVersionUID = 1L;

    private String tamanho;
    private String cor;

    /**
     * Constrói uma nova roupa, herdando o ID gerado automaticamente por {@link Produto}.
     *
     * @param nome       nome da peça de roupa
     * @param preco      preço de venda
     * @param qtdEstoque quantidade inicial em estoque
     * @param tamanho    tamanho da peça (ex: "P", "M", "G")
     * @param cor        cor da peça
     */
    public Roupa(String nome, double preco, int qtdEstoque, String tamanho, String cor) {
        super(nome, preco, qtdEstoque);
        this.tamanho = tamanho;
        this.cor = cor;
    }

    /**
     * {@inheritDoc}
     *
     * <p>Para uma {@code Roupa}, inclui também o tamanho e a cor na descrição.</p>
     */
    @Override
    public String exibirDetalhes() {
        return String.format(
                "Roupa #%d | Nome: %s | Preço: R$ %.2f | Estoque: %d | Tamanho: %s | Cor: %s",
                getId(), getNome(), getPreco(), getQtdEstoque(), tamanho, cor);
    }

    /**
     * Sobrescreve o método toString para exibição limpa e estruturada com ID na listagem do JOptionPane.
     */
    @Override
    public String toString() {
        return String.format("[ID: %d] Roupa: %s - R$ %.2f (Estoque: %d) | Tam: %s, Cor: %s", 
                getId(), getNome(), getPreco(), getQtdEstoque(), tamanho, cor);
    }

    /** @return o tamanho da peça (ex: P, M, G) */
    public String getTamanho() {
        return tamanho;
    }

    /** @param tamanho novo tamanho da peça */
    public void setTamanho(String tamanho) {
        this.tamanho = tamanho;
    }

    /** @return a cor da peça */
    public String getCor() {
        return cor;
    }

    /** @param cor nova cor da peça */
    public void setCor(String cor) {
        this.cor = cor;
    }
}
