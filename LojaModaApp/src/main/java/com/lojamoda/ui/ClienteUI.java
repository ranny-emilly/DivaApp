package com.lojamoda.ui;

import com.lojamoda.exception.CpfInvalidoException;
import com.lojamoda.exception.EntidadeNaoEncontradaException;
import com.lojamoda.model.Cliente;
import com.lojamoda.service.ClienteService;

import javax.swing.JOptionPane;
import java.util.ArrayList;


public class ClienteUI {

    private final ClienteService service;

    public ClienteUI(ClienteService service) {
        this.service = service;
    }

    public void exibirMenu() {
        boolean continuar = true;
        while (continuar) {
            String[] opcoes = {"Incluir", "Consultar", "Alterar", "Excluir", "Listar Todos", "Voltar"};
            int escolha = JOptionPane.showOptionDialog(null,
                    "   Gerenciamento de Clientes \nEscolha uma operação:",
                    "Manter Clientes",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                    null, opcoes, opcoes[0]);

            switch (escolha) {
                case 0 -> incluir();
                case 1 -> consultar();
                case 2 -> alterar();
                case 3 -> excluir();
                case 4 -> listar();
                default -> continuar = false;
            }
        }
    }

    private void incluir() {
        try {
            String nome = JOptionPane.showInputDialog("Nome do cliente:");
            if (nome == null) return;
            String cpf = JOptionPane.showInputDialog("CPF do cliente (somente 11 números):");
            if (cpf == null) return;

            Cliente novo = service.incluir(nome.trim(), cpf.trim());
            JOptionPane.showMessageDialog(null,
                    "Cliente cadastrado com sucesso!\n" + novo,
                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        } catch (CpfInvalidoException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Erro de Validação", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void consultar() {
        Integer id = lerId("Digite o ID do cliente a consultar:");
        if (id == null) return;
        try {
            Cliente cliente = service.consultar(id);
            JOptionPane.showMessageDialog(null, cliente.toString(), "Cliente Encontrado", JOptionPane.INFORMATION_MESSAGE);
        } catch (EntidadeNaoEncontradaException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Não Encontrado", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void alterar() {
        Integer id = lerId("Digite o ID do cliente a alterar:");
        if (id == null) return;
        try {
            Cliente atual = service.consultar(id);

            String novoNome = JOptionPane.showInputDialog("Novo nome:", atual.getNome());
            if (novoNome == null) return;
            String novoCpf = JOptionPane.showInputDialog("Novo CPF:", atual.getCpf());
            if (novoCpf == null) return;

            service.alterar(id, novoNome.trim(), novoCpf.trim());
            JOptionPane.showMessageDialog(null, "Cliente alterado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        } catch (EntidadeNaoEncontradaException | CpfInvalidoException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluir() {
        Integer id = lerId("Digite o ID do cliente a excluir:");
        if (id == null) return;
        try {
            Cliente cliente = service.consultar(id);
            int confirmacao = JOptionPane.showConfirmDialog(null,
                    "Confirma a exclusão de:\n" + cliente + "?",
                    "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);
            if (confirmacao == JOptionPane.YES_OPTION) {
                service.excluir(id);
                JOptionPane.showMessageDialog(null, "Cliente excluído com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (EntidadeNaoEncontradaException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Não Encontrado", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void listar() {
        ArrayList<Cliente> clientes = service.listar();
        if (clientes.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nenhum cliente cadastrado.", "Lista de Clientes", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (Cliente c : clientes) {
            sb.append(c).append("\n");
        }
        JOptionPane.showMessageDialog(null, sb.toString(), "Lista de Clientes", JOptionPane.INFORMATION_MESSAGE);
    }

    private Integer lerId(String mensagem) {
        String texto = JOptionPane.showInputDialog(mensagem);
        if (texto == null) {
            return null;
        }
        try {
            return Integer.parseInt(texto.trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "ID inválido. Digite apenas números.", "Erro", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
}
