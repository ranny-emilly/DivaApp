package com.lojamoda;

import com.lojamoda.exception.CpfInvalidoException;
import com.lojamoda.exception.EntidadeNaoEncontradaException;
import com.lojamoda.exception.EstoqueInsuficienteException;
import com.lojamoda.model.Calcado;
import com.lojamoda.model.Cliente;
import com.lojamoda.model.Pedido;
import com.lojamoda.model.Roupa;
import com.lojamoda.service.ClienteService;
import com.lojamoda.service.PedidoService;
import com.lojamoda.service.ProdutoService;

import java.io.File;
import java.util.ArrayList;

public class TestesFuncionais {

    private static int totalTestes = 0;
    private static int totalFalhas = 0;

    public static void main(String[] args) {
        limparDadosDeTeste();

        System.out.println(" SUÍTE DE TESTES FUNCIONAIS - SISTEMA LOJA DE MODA");

        testeCT01_IncluirClienteComCpfValido();
        testeCT02_IncluirClienteComCpfInvalido_DeveLancarExcecao();
        testeCT03_IncluirClienteComCpfDuplicado_DeveLancarExcecao();
        testeCT04_GeracaoDeIdsSequenciaisParaClientes();
        testeCT05_ConsultarClienteInexistente_DeveLancarExcecao();
        testeCT06_AlterarCliente();
        testeCT07_ExcluirCliente();
        testeCT08_IncluirRoupaEConsultar();
        testeCT09_IncluirCalcadoEConsultar();
        testeCT10_HerancaEPolimorfismo_ExibirDetalhes();
        testeCT11_RegistrarVendaComEstoqueSuficiente();
        testeCT12_RegistrarVendaComEstoqueInsuficiente_DeveLancarExcecao();
        testeCT13_CancelarPedido_DeveDevolverEstoque();
        testeCT14_PersistenciaEmArquivo_DadosSobrevivemAoReinicio();
        testeCT15_CalculoDoValorTotalDoPedido();

        System.out.printf(" RESULTADO FINAL: %d/%d testes passaram%n", (totalTestes - totalFalhas), totalTestes);

        limparDadosDeTeste();
    }

    // ---------------------------------------------------------------------
    // CT01
    // ---------------------------------------------------------------------
    private static void testeCT01_IncluirClienteComCpfValido() {
        iniciarTeste("CT01 - Incluir cliente com CPF válido");
        try {
            ClienteService service = new ClienteService();
            Cliente c = service.incluir("Maria Silva", "12345678901");
            assertTrue(c.getId() > 0, "Cliente deve ter ID maior que zero");
            assertEquals("Maria Silva", c.getNome(), "Nome deve ser igual ao informado");
            assertEquals("12345678901", c.getCpf(), "CPF deve ser igual ao informado");
            sucesso();
        } catch (Exception e) {
            falha(e);
        }
    }

    // ---------------------------------------------------------------------
    // CT02
    // ---------------------------------------------------------------------
    private static void testeCT02_IncluirClienteComCpfInvalido_DeveLancarExcecao() {
        iniciarTeste("CT02 - Incluir cliente com CPF inválido deve lançar CpfInvalidoException");
        try {
            ClienteService service = new ClienteService();
            try {
                service.incluir("João Souza", "123");
                falha(new AssertionError("Era esperado o lançamento de CpfInvalidoException"));
                return;
            } catch (CpfInvalidoException e) {
                sucesso();
            }
        } catch (Exception e) {
            falha(e);
        }
    }

    // ---------------------------------------------------------------------
    // CT03
    // ---------------------------------------------------------------------
    private static void testeCT03_IncluirClienteComCpfDuplicado_DeveLancarExcecao() {
        iniciarTeste("CT03 - Incluir cliente com CPF já cadastrado deve lançar CpfInvalidoException");
        try {
            ClienteService service = new ClienteService();
            service.incluir("Ana Paula", "98765432100");
            try {
                service.incluir("Outro Nome", "98765432100");
                falha(new AssertionError("Era esperado o lançamento de CpfInvalidoException para CPF duplicado"));
                return;
            } catch (CpfInvalidoException e) {
                sucesso();
            }
        } catch (Exception e) {
            falha(e);
        }
    }

    // ---------------------------------------------------------------------
    // CT04
    // ---------------------------------------------------------------------
    private static void testeCT04_GeracaoDeIdsSequenciaisParaClientes() {
        iniciarTeste("CT04 - IDs de clientes devem ser sequenciais e únicos");
        try {
            ClienteService service = new ClienteService();
            Cliente c1 = service.incluir("Cliente Um", "11111111111");
            Cliente c2 = service.incluir("Cliente Dois", "22222222222");
            Cliente c3 = service.incluir("Cliente Tres", "33333333333");
            assertTrue(c2.getId() == c1.getId() + 1, "ID do segundo cliente deve ser sequencial ao primeiro");
            assertTrue(c3.getId() == c2.getId() + 1, "ID do terceiro cliente deve ser sequencial ao segundo");
            sucesso();
        } catch (Exception e) {
            falha(e);
        }
    }

    // ---------------------------------------------------------------------
    // CT05
    // ---------------------------------------------------------------------
    private static void testeCT05_ConsultarClienteInexistente_DeveLancarExcecao() {
        iniciarTeste("CT05 - Consultar cliente com ID inexistente deve lançar EntidadeNaoEncontradaException");
        try {
            ClienteService service = new ClienteService();
            try {
                service.consultar(9999);
                falha(new AssertionError("Era esperado o lançamento de EntidadeNaoEncontradaException"));
                return;
            } catch (EntidadeNaoEncontradaException e) {
                sucesso();
            }
        } catch (Exception e) {
            falha(e);
        }
    }

    // ---------------------------------------------------------------------
    // CT06
    // ---------------------------------------------------------------------
    private static void testeCT06_AlterarCliente() {
        iniciarTeste("CT06 - Alterar dados de um cliente existente");
        try {
            ClienteService service = new ClienteService();
            Cliente c = service.incluir("Nome Antigo", "44444444444");
            service.alterar(c.getId(), "Nome Novo", "55555555555");
            Cliente alterado = service.consultar(c.getId());
            assertEquals("Nome Novo", alterado.getNome(), "Nome deve ter sido atualizado");
            assertEquals("55555555555", alterado.getCpf(), "CPF deve ter sido atualizado");
            sucesso();
        } catch (Exception e) {
            falha(e);
        }
    }

    // ---------------------------------------------------------------------
    // CT07
    // ---------------------------------------------------------------------
    private static void testeCT07_ExcluirCliente() {
        iniciarTeste("CT07 - Excluir cliente existente");
        try {
            ClienteService service = new ClienteService();
            Cliente c = service.incluir("Cliente Excluivel", "66666666666");
            service.excluir(c.getId());
            try {
                service.consultar(c.getId());
                falha(new AssertionError("Cliente excluído não deveria mais ser encontrado"));
                return;
            } catch (EntidadeNaoEncontradaException e) {
                sucesso();
            }
        } catch (Exception e) {
            falha(e);
        }
    }

    // ---------------------------------------------------------------------
    // CT08
    // ---------------------------------------------------------------------
    private static void testeCT08_IncluirRoupaEConsultar() {
        iniciarTeste("CT08 - Incluir roupa e consultar pelo ID gerado");
        try {
            ProdutoService service = new ProdutoService();
            Roupa r = service.incluirRoupa("Camiseta Polo", 89.90, 10, "M", "Azul");
            var encontrado = service.consultar(r.getId());
            assertTrue(encontrado instanceof Roupa, "Produto consultado deve ser do tipo Roupa");
            assertEquals("Camiseta Polo", encontrado.getNome(), "Nome do produto deve corresponder");
            sucesso();
        } catch (Exception e) {
            falha(e);
        }
    }

    // ---------------------------------------------------------------------
    // CT09
    // ---------------------------------------------------------------------
    private static void testeCT09_IncluirCalcadoEConsultar() {
        iniciarTeste("CT09 - Incluir calçado e consultar pelo ID gerado");
        try {
            ProdutoService service = new ProdutoService();
            Calcado c = service.incluirCalcado("Tênis Esportivo", 199.90, 5, 42, "Lona");
            var encontrado = service.consultar(c.getId());
            assertTrue(encontrado instanceof Calcado, "Produto consultado deve ser do tipo Calcado");
            assertEquals(42, ((Calcado) encontrado).getNumeracao(), "Numeração deve corresponder");
            sucesso();
        } catch (Exception e) {
            falha(e);
        }
    }

    // ---------------------------------------------------------------------
    // CT10
    // ---------------------------------------------------------------------
    private static void testeCT10_HerancaEPolimorfismo_ExibirDetalhes() {
        iniciarTeste("CT10 - Herança e polimorfismo: exibirDetalhes() difere entre Roupa e Calcado");
        try {
            Roupa r = new Roupa("Vestido Floral", 150.0, 3, "P", "Vermelho");
            Calcado c = new Calcado("Sandália", 80.0, 7, 36, "Couro");

            String detalhesRoupa = r.exibirDetalhes();
            String detalhesCalcado = c.exibirDetalhes();

            assertTrue(detalhesRoupa.contains("Tamanho"), "Detalhes da roupa devem conter o tamanho");
            assertTrue(detalhesCalcado.contains("Numeração"), "Detalhes do calçado devem conter a numeração");
            assertTrue(!detalhesRoupa.equals(detalhesCalcado), "Polimorfismo: as descrições devem ser diferentes entre subclasses");
            sucesso();
        } catch (Exception e) {
            falha(e);
        }
    }

    // ---------------------------------------------------------------------
    // CT11
    // ---------------------------------------------------------------------
    private static void testeCT11_RegistrarVendaComEstoqueSuficiente() {
        iniciarTeste("CT11 - Registrar venda com estoque suficiente deve dar baixa no estoque");
        try {
            ClienteService clienteService = new ClienteService();
            ProdutoService produtoService = new ProdutoService();
            PedidoService pedidoService = new PedidoService(produtoService);

            Cliente cliente = clienteService.incluir("Comprador Teste", "77777777777");
            Roupa roupa = produtoService.incluirRoupa("Calça Jeans", 120.0, 5, "G", "Azul");

            ArrayList<Integer> ids = new ArrayList<>();
            ids.add(roupa.getId());

            Pedido pedido = pedidoService.registrarVenda(cliente, ids);

            assertEquals(120.0, pedido.getValorTotal(), "Valor total do pedido deve ser igual ao preço do item vendido");
            assertEquals(4, produtoService.consultar(roupa.getId()).getQtdEstoque(), "Estoque deve ter sido reduzido em 1 unidade");
            sucesso();
        } catch (Exception e) {
            falha(e);
        }
    }

    // ---------------------------------------------------------------------
    // CT12
    // ---------------------------------------------------------------------
    private static void testeCT12_RegistrarVendaComEstoqueInsuficiente_DeveLancarExcecao() {
        iniciarTeste("CT12 - Registrar venda com estoque insuficiente deve lançar EstoqueInsuficienteException");
        try {
            ClienteService clienteService = new ClienteService();
            ProdutoService produtoService = new ProdutoService();
            PedidoService pedidoService = new PedidoService(produtoService);

            Cliente cliente = clienteService.incluir("Comprador Dois", "88888888888");
            Roupa roupa = produtoService.incluirRoupa("Jaqueta Couro", 350.0, 1, "M", "Preta");

            ArrayList<Integer> ids = new ArrayList<>();
            ids.add(roupa.getId());
            ids.add(roupa.getId()); // tenta comprar 2 unidades, mas só há 1 em estoque

            try {
                pedidoService.registrarVenda(cliente, ids);
                falha(new AssertionError("Era esperado o lançamento de EstoqueInsuficienteException"));
                return;
            } catch (EstoqueInsuficienteException e) {
                // Verifica que a operação foi atômica: estoque não deve ter sido alterado
                assertEquals(1, produtoService.consultar(roupa.getId()).getQtdEstoque(),
                        "Estoque não deve ser alterado quando a venda falha (atomicidade)");
                sucesso();
            }
        } catch (Exception e) {
            falha(e);
        }
    }

    // ---------------------------------------------------------------------
    // CT13
    // ---------------------------------------------------------------------
    private static void testeCT13_CancelarPedido_DeveDevolverEstoque() {
        iniciarTeste("CT13 - Cancelar pedido deve devolver o estoque dos produtos");
        try {
            ClienteService clienteService = new ClienteService();
            ProdutoService produtoService = new ProdutoService();
            PedidoService pedidoService = new PedidoService(produtoService);

            Cliente cliente = clienteService.incluir("Comprador Tres", "99999999999");
            Calcado calcado = produtoService.incluirCalcado("Bota", 250.0, 3, 40, "Couro");

            ArrayList<Integer> ids = new ArrayList<>();
            ids.add(calcado.getId());
            Pedido pedido = pedidoService.registrarVenda(cliente, ids);

            assertEquals(2, produtoService.consultar(calcado.getId()).getQtdEstoque(), "Estoque deve estar reduzido após a venda");

            pedidoService.excluir(pedido.getIdPedido());

            assertEquals(3, produtoService.consultar(calcado.getId()).getQtdEstoque(), "Estoque deve ser devolvido após o cancelamento");
            sucesso();
        } catch (Exception e) {
            falha(e);
        }
    }

    // ---------------------------------------------------------------------
    // CT14
    // ---------------------------------------------------------------------
    private static void testeCT14_PersistenciaEmArquivo_DadosSobrevivemAoReinicio() {
        iniciarTeste("CT14 - Dados persistidos em arquivo devem sobreviver à reinicialização da aplicação (simulada)");
        try {
            ClienteService service1 = new ClienteService();
            Cliente original = service1.incluir("Persistencia Teste", "10101010101");

            // Simula o reinício da aplicação criando uma nova instância do serviço,
            // que deve carregar os dados gravados em arquivo pela instância anterior.
            ClienteService service2 = new ClienteService();
            Cliente recuperado = service2.consultar(original.getId());

            assertEquals(original.getNome(), recuperado.getNome(), "Nome do cliente recuperado deve ser igual ao original");
            assertEquals(original.getCpf(), recuperado.getCpf(), "CPF do cliente recuperado deve ser igual ao original");
            sucesso();
        } catch (Exception e) {
            falha(e);
        }
    }

    // ---------------------------------------------------------------------
    // CT15
    // ---------------------------------------------------------------------
    private static void testeCT15_CalculoDoValorTotalDoPedido() {
        iniciarTeste("CT15 - Valor total do pedido deve ser a soma dos preços dos produtos");
        try {
            ClienteService clienteService = new ClienteService();
            ProdutoService produtoService = new ProdutoService();
            PedidoService pedidoService = new PedidoService(produtoService);

            Cliente cliente = clienteService.incluir("Comprador Multiplo", "12121212121");
            Roupa camisa = produtoService.incluirRoupa("Camisa Social", 99.90, 10, "M", "Branca");
            Calcado sapato = produtoService.incluirCalcado("Sapato Social", 220.10, 10, 41, "Couro");

            ArrayList<Integer> ids = new ArrayList<>();
            ids.add(camisa.getId());
            ids.add(sapato.getId());

            Pedido pedido = pedidoService.registrarVenda(cliente, ids);

            assertEquals(320.0, Math.round(pedido.getValorTotal() * 100.0) / 100.0,
                    "Valor total deve ser a soma dos preços dos dois produtos");
            sucesso();
        } catch (Exception e) {
            falha(e);
        }
    }

    // =====================================================================
    // Infraestrutura simples de teste (sem dependências externas)
    // =====================================================================

    private static void iniciarTeste(String nome) {
        totalTestes++;
        System.out.print("[ " + nome + " ] ... ");
    }

    private static void sucesso() {
        System.out.println("PASSOU ✔");
    }

    private static void falha(Throwable t) {
        totalFalhas++;
        System.out.println("FALHOU ✘ -> " + t.getMessage());
    }

    private static void assertTrue(boolean condicao, String mensagem) {
        if (!condicao) {
            throw new AssertionError(mensagem);
        }
    }

    private static void assertEquals(Object esperado, Object atual, String mensagem) {
        if (esperado == null ? atual != null : !esperado.equals(atual)) {
            throw new AssertionError(mensagem + " | Esperado: " + esperado + " | Atual: " + atual);
        }
    }

    private static void limparDadosDeTeste() {
        deletarSeExistir("dados/clientes.dat");
        deletarSeExistir("dados/produtos.dat");
        deletarSeExistir("dados/pedidos.dat");
    }

    private static void deletarSeExistir(String caminho) {
        File f = new File(caminho);
        if (f.exists()) {
            f.delete();
        }
    }
}
