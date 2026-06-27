package com.lojamoda.model;

import java.io.Serializable;

/**
 * Representa um cliente da loja.
 *
 * <p>Possui um identificador único gerado automaticamente de forma
 * sequencial (atributo estático {@code contadorId}), além de nome e CPF.</p>
 *
 * @author Elisa Correia, Emilly Ranny, Jolie Pavan
 */
public class Cliente implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Contador estático utilizado para gerar IDs sequenciais e únicos. */
    private static int contadorId = 1;

    private final int id;
    private String nome;
    private String cpf;

    /**
     * Constrói um novo cliente, atribuindo automaticamente um ID único e sequencial.
     *
     * @param nome nome completo do cliente
     * @param cpf  CPF do cliente (somente dígitos, 11 caracteres)
     */
    public Cliente(String nome, String cpf) {
        this.id = contadorId++;
        this.nome = nome;
        this.cpf = cpf;
    }

    /**
     * Ajusta o contador estático de IDs para um valor específico.
     *
     * <p>Utilizado exclusivamente pela camada de persistência ao carregar
     * dados já existentes de arquivo, evitando colisão de IDs entre
     * clientes carregados e novos clientes criados na sessão atual.</p>
     *
     * @param valor próximo valor a ser utilizado pelo contador
     */
    public static void ajustarContador(int valor) {
        if (valor > contadorId) {
            contadorId = valor;
        }
    }

    /** @return o identificador único do cliente */
    public int getId() {
        return id;
    }

    /** @return o nome do cliente */
    public String getNome() {
        return nome;
    }

    /** @param nome novo nome do cliente */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /** @return o CPF do cliente */
    public String getCpf() {
        return cpf;
    }

    /** @param cpf novo CPF do cliente */
    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    @Override
    public String toString() {
        return String.format("Cliente #%d | Nome: %s | CPF: %s", id, nome, cpf);
    }
}
