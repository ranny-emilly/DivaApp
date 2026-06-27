package com.lojamoda.ui;

import com.lojamoda.exception.EntidadeNaoEncontradaException;
import com.lojamoda.exception.EstoqueInsuficienteException;
import com.lojamoda.model.Cliente;
import com.lojamoda.model.Pedido;
import com.lojamoda.model.Produto;
import com.lojamoda.service.ClienteService;
import com.lojamoda.service.PedidoService;
import com.lojamoda.service.ProdutoService;

import javax.swing.JOptionPane;
import java.util.ArrayList;

/**
 * Interface gráfica (baseada em {@link JOptionPane}) para o registro de
 * vendas (pedidos), implementando o caso de uso <b>UC03: Registrar
 * Venda</b>, além das consultas e exclusões (cancelamento) relacionadas.
 *
 * @author Elisa Correia, Emilly Ranny, Jolie Pavan
 */
public class PedidoUI {

    private final PedidoService pedidoService;
    private final ClienteService clienteService;
    private final ProdutoService produtoService;

    /**
     * Cria a interface de pedidos associada aos serviços informados.
     *
     * @param pedidoService  serviço de pedidos utilizado para registrar vendas
     * @param clienteService serviço de clientes utilizado para selecionar o comprador
     * @param produtoService serviço de produtos utilizado para selecionar os itens vendidos
     */
    public PedidoUI(PedidoService pedidoService, ClienteService clienteService, ProdutoService produtoService) {
        this.pedidoService = pedidoService;
        this.clienteService = clienteService;
        this.produtoService = produtoService;
    }

    /**
     * Exibe o menu principal de gerenciamento de pedidos e processa a
     * opção escolhida pelo usuário em loop, até que ele opte por voltar.
     */
    public void exibirMenu() {
        boolean continuar = true;
        while (continuar) {
            String[] opcoes = {"Registrar Venda", "Consultar Pedido", "Cancelar Pedido", "Listar Todos", "Voltar"};
            int escolha = JOptionPane.showOptionDialog(null,
                    "=== Gerenciamento de Pedidos ===\nEscolha uma operação:",
                    "Manter Pedidos",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                    null, opcoes, opcoes[0]);

            switch (escolha) {
                case 0 -> registrarVenda();
                case 1 -> consultar();
                case 2 -> cancelar();
                case 3 -> listar();
                default -> continuar = false;
            }
        }
    }

    private void registrarVenda() {
        if (clienteService.listar().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Não há clientes cadastrados. Cadastre um cliente antes de registrar uma venda.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (produtoService.listar().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Não há produtos cadastrados. Cadastre um produto antes de registrar uma venda.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Integer idCliente = lerInt("Digite o ID do cliente para esta venda:");
        if (idCliente == null) return;

        Cliente cliente;
        try {
            cliente = clienteService.consultar(idCliente);
        } catch (EntidadeNaoEncontradaException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        ArrayList<Integer> idsProdutos = new ArrayList<>();
        boolean adicionandoItens = true;
        while (adicionandoItens) {
            mostrarCatalogoResumido();
            Integer idProduto = lerInt("Digite o ID do produto a adicionar ao pedido\n(ou cancele para finalizar a lista de itens):");
            if (idProduto == null) {
                adicionandoItens = false;
                continue;
            }
            try {
                produtoService.consultar(idProduto); // valida existência antecipadamente
                idsProdutos.add(idProduto);
            } catch (EntidadeNaoEncontradaException e) {
                JOptionPane.showMessageDialog(null, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                continue;
            }

            int continuarAdicionando = JOptionPane.showConfirmDialog(null,
                    "Item adicionado! Deseja adicionar outro produto a este pedido?",
                    "Continuar Pedido", JOptionPane.YES_NO_OPTION);
            if (continuarAdicionando != JOptionPane.YES_OPTION) {
                adicionandoItens = false;
            }
        }

        if (idsProdutos.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nenhum produto foi adicionado. Venda cancelada.",
                    "Operação Cancelada", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        try {
            Pedido pedido = pedidoService.registrarVenda(cliente, idsProdutos);
            JOptionPane.showMessageDialog(null,
                    "Venda registrada com sucesso!\n\n" + pedido,
                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        } catch (EntidadeNaoEncontradaException | EstoqueInsuficienteException e) {
            JOptionPane.showMessageDialog(null,
                    "Não foi possível concluir a venda:\n" + e.getMessage(),
                    "Erro na Venda", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarCatalogoResumido() {
        ArrayList<Produto> produtos = produtoService.listar();
        StringBuilder sb = new StringBuilder("Catálogo disponível:\n");
        for (Produto p : produtos) {
            sb.append(String.format("#%d - %s (Estoque: %d)%n", p.getId(), p.getNome(), p.getQtdEstoque()));
        }
        JOptionPane.showMessageDialog(null, sb.toString(), "Catálogo", JOptionPane.PLAIN_MESSAGE);
    }

    private void consultar() {
        Integer id = lerInt("Digite o ID do pedido a consultar:");
        if (id == null) return;
        try {
            Pedido pedido = pedidoService.consultar(id);
            JOptionPane.showMessageDialog(null, pedido.toString(), "Pedido Encontrado", JOptionPane.INFORMATION_MESSAGE);
        } catch (EntidadeNaoEncontradaException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Não Encontrado", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void cancelar() {
        Integer id = lerInt("Digite o ID do pedido a cancelar:");
        if (id == null) return;
        try {
            Pedido pedido = pedidoService.consultar(id);
            int confirmacao = JOptionPane.showConfirmDialog(null,
                    "Confirma o cancelamento do pedido a seguir?\n(O estoque dos produtos será devolvido)\n\n" + pedido,
                    "Confirmar Cancelamento", JOptionPane.YES_NO_OPTION);
            if (confirmacao == JOptionPane.YES_OPTION) {
                pedidoService.excluir(id);
                JOptionPane.showMessageDialog(null, "Pedido cancelado com sucesso! Estoque devolvido.", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (EntidadeNaoEncontradaException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Não Encontrado", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void listar() {
        ArrayList<Pedido> pedidos = pedidoService.listar();
        if (pedidos.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nenhum pedido registrado.", "Lista de Pedidos", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (Pedido p : pedidos) {
            sb.append(p).append("\n");
        }
        JOptionPane.showMessageDialog(null, sb.toString(), "Lista de Pedidos", JOptionPane.INFORMATION_MESSAGE);
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
}
