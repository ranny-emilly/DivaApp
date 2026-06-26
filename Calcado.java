package com.lojamoda.model;

/**
 * Representa um calçado do catálogo da loja.
 *
 * <p>É uma especialização (herança) de {@link Produto}, adicionando os
 * atributos específicos de calçados: numeração e material.</p>
 *
 * <p>Esta classe, junto com {@link Roupa}, demonstra o uso de herança e
 * polimorfismo no modelo: ambas estendem {@link Produto} e implementam
 * {@code exibirDetalhes()} de forma própria, conforme exigido pelo
 * requisito RF02 (gerenciamento de Roupas e Calçados).</p>
 *
 * @author Elisa Correia, Emilly Ranny, Jolie Pavan
 */
public class Calcado extends Produto {

    private static final long serialVersionUID = 1L;

    private int numeracao;
    private String material;

    /**
     * Constrói um novo calçado, herdando o ID gerado automaticamente por {@link Produto}.
     *
     * @param nome       nome do calçado
     * @param preco      preço de venda
     * @param qtdEstoque quantidade inicial em estoque
     * @param numeracao  numeração do calçado (ex: 38, 40, 42)
     * @param material   material de fabricação (ex: "Couro", "Lona")
     */
    public Calcado(String nome, double preco, int qtdEstoque, int numeracao, String material) {
        super(nome, preco, qtdEstoque);
        this.numeracao = numeracao;
        this.material = material;
    }

    /**
     * {@inheritDoc}
     *
     * <p>Para um {@code Calcado}, inclui também a numeração e o material na descrição.</p>
     */
    @Override
    public String exibirDetalhes() {
        return String.format(
                "Calçado #%d | Nome: %s | Preço: R$ %.2f | Estoque: %d | Numeração: %d | Material: %s",
                getId(), getNome(), getPreco(), getQtdEstoque(), numeracao, material);
    }

    /** @return a numeração do calçado */
    public int getNumeracao() {
        return numeracao;
    }

    /** @param numeracao nova numeração do calçado */
    public void setNumeracao(int numeracao) {
        this.numeracao = numeracao;
    }

    /** @return o material de fabricação do calçado */
    public String getMaterial() {
        return material;
    }

    /** @param material novo material de fabricação */
    public void setMaterial(String material) {
        this.material = material;
    }
}
