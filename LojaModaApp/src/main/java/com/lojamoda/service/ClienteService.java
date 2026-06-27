package com.lojamoda.service;

import com.lojamoda.exception.CpfInvalidoException;
import com.lojamoda.exception.EntidadeNaoEncontradaException;
import com.lojamoda.model.Cliente;
import com.lojamoda.persistence.ArquivoDAO;

import java.io.IOException;
import java.util.ArrayList;

public class ClienteService {

    private static final String ARQUIVO_CLIENTES = "dados/clientes.dat";

    private final ArrayList<Cliente> clientes;
    private final ArquivoDAO<Cliente> dao;

    public ClienteService() {
        this.dao = new ArquivoDAO<>(ARQUIVO_CLIENTES);
        this.clientes = carregarDoArquivo();
        ajustarContadorAposCarga();
    }

    private ArrayList<Cliente> carregarDoArquivo() {
        try {
            return dao.carregar();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Aviso: não foi possível carregar clientes.dat (" + e.getMessage() + "). Iniciando lista vazia.");
            return new ArrayList<>();
        }
    }

    private void ajustarContadorAposCarga() {
        int maiorId = 0;
        for (Cliente c : clientes) {
            if (c.getId() > maiorId) {
                maiorId = c.getId();
            }
        }
        Cliente.ajustarContador(maiorId + 1);
    }

    public Cliente incluir(String nome, String cpf) throws CpfInvalidoException {
        validarCpf(cpf);
        Cliente novo = new Cliente(nome, cpf);
        clientes.add(novo);
        persistir();
        return novo;
    }

    private void validarCpf(String cpf) throws CpfInvalidoException {
        if (cpf == null || !cpf.matches("\\d{11}")) {
            throw new CpfInvalidoException("CPF inválido. Informe exatamente 11 dígitos numéricos.");
        }
        for (Cliente c : clientes) {
            if (c.getCpf().equals(cpf)) {
                throw new CpfInvalidoException("Já existe um cliente cadastrado com o CPF " + cpf + ".");
            }
        }
    }

    public Cliente consultar(int id) throws EntidadeNaoEncontradaException {
        for (Cliente c : clientes) {
            if (c.getId() == id) {
                return c;
            }
        }
        throw new EntidadeNaoEncontradaException("Cliente", id);
    }

    public void alterar(int id, String novoNome, String novoCpf) throws EntidadeNaoEncontradaException, CpfInvalidoException {
        Cliente cliente = consultar(id);
        if (!cliente.getCpf().equals(novoCpf)) {
            validarCpf(novoCpf);
        }
        cliente.setNome(novoNome);
        cliente.setCpf(novoCpf);
        persistir();
    }

    public void excluir(int id) throws EntidadeNaoEncontradaException {
        Cliente cliente = consultar(id);
        clientes.remove(cliente);
        persistir();
    }

    public ArrayList<Cliente> listar() {
        return new ArrayList<>(clientes);
    }

    private void persistir() {
        try {
            dao.salvar(clientes);
        } catch (IOException e) {
            System.err.println("Erro ao salvar clientes.dat: " + e.getMessage());
        }
    }
}
