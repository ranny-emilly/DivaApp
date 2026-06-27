package com.lojamoda.model;

public class Calcado extends Produto {

    private static final long serialVersionUID = 1L;

    private int numeracao;
    private String material;

    public Calcado(String nome, double preco, int qtdEstoque, int numeracao, String material) {
        super(nome, preco, qtdEstoque);
        this.numeracao = numeracao;
        this.material = material;
    }

    @Override
    public String exibirDetalhes() {
        return String.format(
                "Calçado #%d | Nome: %s | Preço: R$ %.2f | Estoque: %d | Numeração: %d | Material: %s",
                getId(), getNome(), getPreco(), getQtdEstoque(), numeracao, material);
    }

    public int getNumeracao() {
        return numeracao;
    }

    public void setNumeracao(int numeracao) {
        this.numeracao = numeracao;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }
}
