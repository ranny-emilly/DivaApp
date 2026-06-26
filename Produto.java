package com.lojamoda.model;

import java.io.Serializable;

public abstract class Produto implements Serializable {

    private static final long serialVersionUID = 1L;

    private static int contadorId = 1;

    private final int id;
    private String nome;
    private double preco;
    private int qtdEstoque;


    protected Produto(String nome, double preco, int qtdEstoque) {
        this.id = contadorId++;
        this.nome = nome;
        this.preco = preco;
        this.qtdEstoque = qtdEstoque;
    }

    public static void ajustarContador(int valor) {
        if (valor > contadorId) {
            contadorId = valor;
        }
    }

    public abstract String exibirDetalhes();

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public int getQtdEstoque() {
        return qtdEstoque;
    }

    public void setQtdEstoque(int qtdEstoque) {
        this.qtdEstoque = qtdEstoque;
    }

    @Override
    public String toString() {
    return "[ID: " + this.id + "] " + this.nome + " - R$ " + String.format("%.2f", this.preco) + " (Estoque: " + this.qtdEstoque + ")";
    }
}
