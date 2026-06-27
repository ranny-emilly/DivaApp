package com.lojamoda;

import com.lojamoda.service.ClienteService;
import com.lojamoda.service.PedidoService;
import com.lojamoda.service.ProdutoService;
import com.lojamoda.ui.ClienteUI;
import com.lojamoda.ui.PedidoUI;
import com.lojamoda.ui.ProdutoUI;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

/**
 * Classe principal da aplicação <b>Sistema de Gerenciamento de Loja de
 * Moda</b>.
 *
 * <p>Implementa o caso de uso <b>UC04: Persistir Dados</b> de forma
 * transparente: ao iniciar, cada serviço carrega automaticamente os
 * dados persistidos em arquivo; a cada operação de inclusão, alteração
 * ou exclusão, os dados são imediatamente regravados em disco, garantindo
 * que nenhuma informação seja perdida mesmo que o programa seja
 * encerrado de forma abrupta.</p>
 *
 * <p>A interação com o usuário ocorre inteiramente através de caixas de
 * diálogo {@link JOptionPane} (RF06), conforme exigido pelo escopo do
 * trabalho.</p>
 *
 * @author Elisa Correia, Emilly Ranny, Jolie Pavan
 */
public class Main {

    /**
     * Ponto de entrada da aplicação.
     *
     * @param args argumentos de linha de comando (não utilizados)
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
            // Caso o look and feel do sistema não esteja disponível, mantém o padrão.
        }

        ClienteService clienteService = new ClienteService();
        ProdutoService produtoService = new ProdutoService();
        PedidoService pedidoService = new PedidoService(produtoService);

        ClienteUI clienteUI = new ClienteUI(clienteService);
        ProdutoUI produtoUI = new ProdutoUI(produtoService);
        PedidoUI pedidoUI = new PedidoUI(pedidoService, clienteService, produtoService);

        JOptionPane.showMessageDialog(null,
                "Bem-vindo ao Sistema de Gerenciamento de Loja de Moda!\n" +
                        "Todos os dados são salvos automaticamente em arquivos locais.",
                "Loja de Moda", JOptionPane.INFORMATION_MESSAGE);

        boolean continuar = true;
        while (continuar) {
            String[] opcoes = {"Manter Clientes", "Manter Produtos", "Registrar/Gerenciar Vendas", "Sair"};
            int escolha = JOptionPane.showOptionDialog(null,
                    "=== MENU PRINCIPAL ===\nEscolha uma opção:",
                    "Loja de Moda - Sistema de Gerenciamento",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                    null, opcoes, opcoes[0]);

            switch (escolha) {
                case 0 -> clienteUI.exibirMenu();
                case 1 -> produtoUI.exibirMenu();
                case 2 -> pedidoUI.exibirMenu();
                default -> continuar = false;
            }
        }

        JOptionPane.showMessageDialog(null,
                "Todos os dados foram salvos. Até a próxima!",
                "Encerrando", JOptionPane.INFORMATION_MESSAGE);
    }
}
