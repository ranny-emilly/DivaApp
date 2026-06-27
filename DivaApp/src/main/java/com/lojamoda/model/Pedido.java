package com.lojamoda.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;


public class Pedido implements Serializable {

    private static final long serialVersionUID = 1L;

    private static int contadorId = 1;

    private final int idPedido;
    private final Cliente cliente;
    private final ArrayList<Produto> produtos;
    private double valorTotal;
    private final LocalDate data;

    public Pedido(Cliente cliente) {
        this.idPedido = contadorId++;
        this.cliente = cliente;
        this.produtos = new ArrayList<>();
        this.valorTotal = 0.0;
        this.data = LocalDate.now();
    }

    public static void ajustarContador(int valor) {
        if (valor > contadorId) {
            contadorId = valor;
        }
    }

    public void adicionarProduto(Produto p) {
        this.produtos.add(p);
        calcularTotal();
    }

    public double calcularTotal() {
        double total = 0.0;
        for (Produto p : produtos) {
            total += p.getPreco();
        }
        this.valorTotal = total;
        return this.valorTotal;
    }

    public int getIdPedido() {
        return idPedido;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public ArrayList<Produto> getProdutos() {
        return produtos;
    }

    public double getValorTotal() {
        return valorTotal;
    }

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
