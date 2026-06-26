package com.lojamoda.service;

import com.lojamoda.exception.CpfInvalidoException;
import com.lojamoda.exception.EntidadeNaoEncontradaException;
import com.lojamoda.model.Cliente;
import com.lojamoda.persistence.ArquivoDAO;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Camada de serviço responsável pelas regras de negócio e operações de
 * CRUD (Inclusão, Exclusão, Alteração, Consulta e Listagem) sobre
 * {@link Cliente}.
 *
 * <p>Mantém a lista de clientes em memória e delega a persistência em
 * arquivo binário ao {@link ArquivoDAO}.</p>
 *
 * @author Elisa Correia, Emilly Ranny, Jolie Pavan
 */
public class ClienteService {

    private static final String ARQUIVO_CLIENTES = "dados/clientes.dat";

    private final ArrayList<Cliente> clientes;
    private final ArquivoDAO<Cliente> dao;

    /**
     * Cria o serviço de clientes, carregando automaticamente os dados
     * já persistidos em arquivo (se existentes) e ajustando o contador
     * de IDs para evitar colisões.
     */
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

    /**
     * Inclui um novo cliente, validando o formato do CPF e garantindo
     * que não exista outro cliente já cadastrado com o mesmo CPF.
     *
     * @param nome nome do cliente
     * @param cpf  CPF do cliente (somente dígitos, 11 caracteres)
     * @return o cliente recém-criado, já com seu ID gerado automaticamente
     * @throws CpfInvalidoException se o CPF informado não tiver 11 dígitos numéricos
     *                               ou já estiver cadastrado para outro cliente
     */
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

    /**
     * Consulta um cliente pelo seu identificador único.
     *
     * @param id identificador do cliente
     * @return o cliente correspondente
     * @throws EntidadeNaoEncontradaException se nenhum cliente possuir o ID informado
     */
    public Cliente consultar(int id) throws EntidadeNaoEncontradaException {
        for (Cliente c : clientes) {
            if (c.getId() == id) {
                return c;
            }
        }
        throw new EntidadeNaoEncontradaException("Cliente", id);
    }

    /**
     * Altera o nome e o CPF de um cliente já existente.
     *
     * @param id      identificador do cliente a ser alterado
     * @param novoNome novo nome do cliente
     * @param novoCpf  novo CPF do cliente
     * @throws EntidadeNaoEncontradaException se nenhum cliente possuir o ID informado
     * @throws CpfInvalidoException           se o novo CPF for inválido ou já pertencer a outro cliente
     */
    public void alterar(int id, String novoNome, String novoCpf) throws EntidadeNaoEncontradaException, CpfInvalidoException {
        Cliente cliente = consultar(id);
        if (!cliente.getCpf().equals(novoCpf)) {
            validarCpf(novoCpf);
        }
        cliente.setNome(novoNome);
        cliente.setCpf(novoCpf);
        persistir();
    }

    /**
     * Exclui um cliente da base de dados.
     *
     * @param id identificador do cliente a ser excluído
     * @throws EntidadeNaoEncontradaException se nenhum cliente possuir o ID informado
     */
    public void excluir(int id) throws EntidadeNaoEncontradaException {
        Cliente cliente = consultar(id);
        clientes.remove(cliente);
        persistir();
    }

    /**
     * Lista todos os clientes cadastrados.
     *
     * @return uma cópia da lista de clientes cadastrados
     */
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
