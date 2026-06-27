package com.lojamoda.model;


public class Roupa extends Produto {

    private static final long serialVersionUID = 1L;

    private String tamanho;
    private String cor;

   
    public Roupa(String nome, double preco, int qtdEstoque, String tamanho, String cor) {
        super(nome, preco, qtdEstoque);
        this.tamanho = tamanho;
        this.cor = cor;
    }

    @Override
    public String exibirDetalhes() {
        return String.format(
                "Roupa #%d | Nome: %s | Preço: R$ %.2f | Estoque: %d | Tamanho: %s | Cor: %s",
                getId(), getNome(), getPreco(), getQtdEstoque(), tamanho, cor);
    }

    public String getTamanho() {
        return tamanho;
    }

    public void setTamanho(String tamanho) {
        this.tamanho = tamanho;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }
}
