package com.lojamoda.ui;

import com.lojamoda.exception.EntidadeNaoEncontradaException;
import com.lojamoda.model.Calcado;
import com.lojamoda.model.Produto;
import com.lojamoda.model.Roupa;
import com.lojamoda.service.ProdutoService;

import javax.swing.JOptionPane;
import java.util.ArrayList;

/**
 * Interface gráfica (baseada em {@link JOptionPane}) para o gerenciamento
 * completo (CRUD) do catálogo de {@link Produto} (Roupas e Calçados),
 * implementando o caso de uso <b>UC02: Manter Produtos</b>.
 *
 * @author Elisa Correia, Emilly Ranny, Jolie Pavan
 */
public class ProdutoUI {

    private final ProdutoService service;

    /**
     * Cria a interface de produtos associada ao serviço informado.
     *
     * @param service serviço de produtos utilizado para executar as operações de negócio
     */
    public ProdutoUI(ProdutoService service) {
        this.service = service;
    }

    /**
     * Exibe o menu principal de gerenciamento de produtos e processa a
     * opção escolhida pelo usuário em loop, até que ele opte por voltar.
     */
    public void exibirMenu() {
        boolean continuar = true;
        while (continuar) {
            String[] opcoes = {"Incluir", "Consultar", "Alterar", "Excluir", "Listar Todos", "Voltar"};
            int escolha = JOptionPane.showOptionDialog(null,
                    "=== Gerenciamento de Produtos (Roupas e Calçados) ===\nEscolha uma operação:",
                    "Manter Produtos",
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
        String[] tipos = {"Roupa", "Calçado"};
        int tipoEscolhido = JOptionPane.showOptionDialog(null,
                "Qual tipo de produto deseja cadastrar?",
                "Novo Produto", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, tipos, tipos[0]);

        if (tipoEscolhido == JOptionPane.CLOSED_OPTION) return;

        try {
            String nome = JOptionPane.showInputDialog("Nome do produto:");
            if (nome == null) return;
            Double preco = lerDouble("Preço (R$):");
            if (preco == null) return;
            Integer qtd = lerInt("Quantidade em estoque:");
            if (qtd == null) return;

            if (tipoEscolhido == 0) {
                String tamanho = JOptionPane.showInputDialog("Tamanho (P, M, G):");
                if (tamanho == null) return;
                String cor = JOptionPane.showInputDialog("Cor:");
                if (cor == null) return;
                Roupa nova = service.incluirRoupa(nome.trim(), preco, qtd, tamanho.trim(), cor.trim());
                JOptionPane.showMessageDialog(null, "Roupa cadastrada com sucesso!\n" + nova.exibirDetalhes(),
                        "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } else {
                Integer numeracao = lerInt("Numeração:");
                if (numeracao == null) return;
                String material = JOptionPane.showInputDialog("Material:");
                if (material == null) return;
                Calcado novo = service.incluirCalcado(nome.trim(), preco, qtd, numeracao, material.trim());
                JOptionPane.showMessageDialog(null, "Calçado cadastrado com sucesso!\n" + novo.exibirDetalhes(),
                        "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Erro de Validação", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void consultar() {
        Integer id = lerInt("Digite o ID do produto a consultar:");
        if (id == null) return;
        try {
            Produto produto = service.consultar(id);
            JOptionPane.showMessageDialog(null, produto.exibirDetalhes(), "Produto Encontrado", JOptionPane.INFORMATION_MESSAGE);
        } catch (EntidadeNaoEncontradaException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Não Encontrado", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void alterar() {
        Integer id = lerInt("Digite o ID do produto a alterar:");
        if (id == null) return;
        try {
            Produto atual = service.consultar(id);

            String novoNome = JOptionPane.showInputDialog("Novo nome:", atual.getNome());
            if (novoNome == null) return;
            Double novoPreco = lerDouble("Novo preço (atual: " + atual.getPreco() + "):");
            if (novoPreco == null) return;
            Integer novaQtd = lerInt("Nova quantidade em estoque (atual: " + atual.getQtdEstoque() + "):");
            if (novaQtd == null) return;

            service.alterar(id, novoNome.trim(), novoPreco, novaQtd);
            JOptionPane.showMessageDialog(null, "Produto alterado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        } catch (EntidadeNaoEncontradaException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluir() {
        Integer id = lerInt("Digite o ID do produto a excluir:");
        if (id == null) return;
        try {
            Produto produto = service.consultar(id);
            int confirmacao = JOptionPane.showConfirmDialog(null,
                    "Confirma a exclusão de:\n" + produto.exibirDetalhes() + "?",
                    "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);
            if (confirmacao == JOptionPane.YES_OPTION) {
                service.excluir(id);
                JOptionPane.showMessageDialog(null, "Produto excluído com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (EntidadeNaoEncontradaException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Não Encontrado", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void listar() {
        ArrayList<Produto> produtos = service.listar();
        if (produtos.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nenhum produto cadastrado.", "Lista de Produtos", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (Produto p : produtos) {
            sb.append(p.exibirDetalhes()).append("\n");
        }
        JOptionPane.showMessageDialog(null, sb.toString(), "Lista de Produtos", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Solicita ao usuário um valor inteiro via caixa de diálogo.
     *
     * @param mensagem texto exibido na caixa de diálogo
     * @return o valor informado, ou {@code null} se o usuário cancelar ou digitar um valor inválido
     */
    private Integer lerInt(String mensagem) {
        String texto = JOptionPane.showInputDialog(mensagem);
        if (texto == null) return null;
        try {
            return Integer.parseInt(texto.trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Valor inválido. Digite apenas números inteiros.", "Erro", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    /**
     * Solicita ao usuário um valor decimal (preço) via caixa de diálogo.
     *
     * @param mensagem texto exibido na caixa de diálogo
     * @return o valor informado, ou {@code null} se o usuário cancelar ou digitar um valor inválido
     */
    private Double lerDouble(String mensagem) {
        String texto = JOptionPane.showInputDialog(mensagem);
        if (texto == null) return null;
        try {
            double valor = Double.parseDouble(texto.trim().replace(",", "."));
            if (valor < 0) {
                JOptionPane.showMessageDialog(null, "O preço não pode ser negativo.", "Erro", JOptionPane.ERROR_MESSAGE);
                return null;
            }
            return valor;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Valor inválido. Digite um número (ex: 99.90).", "Erro", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
}
