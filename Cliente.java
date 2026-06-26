package com.lojamoda.model;

import java.io.Serializable;

public class Cliente implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Contador estático utilizado para gerar IDs sequenciais e únicos. */
    private static int contadorId = 1;

    private final int id;
    private String nome;
    private String cpf;

    public Cliente(String nome, String cpf) {
        this.id = contadorId++;
        this.nome = nome;
        this.cpf = cpf;
    }

    public static void ajustarContador(int valor) {
        if (valor > contadorId) {
            contadorId = valor;
        }
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    @Override
public String toString() {
    return "[ID: " + this.id + "] Nome: " + this.nome + " | CPF: " + this.cpf;
    }
}
