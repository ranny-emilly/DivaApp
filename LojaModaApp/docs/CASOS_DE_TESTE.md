# Casos de Teste Funcionais — Sistema de Gerenciamento de Loja de Moda

Este documento descreve os casos de teste funcionais elaborados para validar
os requisitos funcionais (RF01–RF06) do sistema. Os mesmos cenários estão
automatizados na classe `com.lojamoda.TestesFuncionais`
(`src/test/java/com/lojamoda/TestesFuncionais.java`), que pode ser executada
via `./build.sh` ou diretamente com `java com.lojamoda.TestesFuncionais`.

> Convenção: cada caso de teste possui um identificador (CTxx), o requisito
> relacionado, a entrada, o resultado esperado e o status (validado pela
> suíte automatizada).

---

## Módulo: Gerenciamento de Clientes (UC01 / RF01, RF04)

| ID | Descrição | Entrada | Resultado Esperado | Automatizado? |
|----|-----------|---------|---------------------|----------------|
| CT01 | Incluir cliente com CPF válido | Nome: "Maria Silva", CPF: "12345678901" | Cliente criado com ID > 0, nome e CPF persistidos corretamente | Sim |
| CT02 | Incluir cliente com CPF inválido (menos de 11 dígitos) | CPF: "123" | Sistema lança `CpfInvalidoException` e **não** cria o cliente | Sim |
| CT03 | Incluir cliente com CPF já cadastrado | Dois clientes com CPF "98765432100" | Segunda inclusão lança `CpfInvalidoException` | Sim |
| CT04 | Geração de IDs sequenciais e únicos | Incluir 3 clientes em sequência | IDs gerados são sequenciais (ex: 1, 2, 3) | Sim |
| CT05 | Consultar cliente com ID inexistente | ID: 9999 | Sistema lança `EntidadeNaoEncontradaException` | Sim |
| CT06 | Alterar dados de um cliente existente | Alterar nome e CPF de um cliente cadastrado | Dados atualizados refletidos na consulta posterior | Sim |
| CT07 | Excluir cliente existente | Excluir cliente cadastrado | Cliente não é mais encontrado em consultas subsequentes | Sim |
| CT-manual-01 | Cancelar inclusão de cliente na interface gráfica (botão "Cancelar" no JOptionPane) | Usuário clica em "Cancelar" durante a inclusão | Operação é abortada sem alterar a base de dados | Manual (UI) |
| CT-manual-02 | Listar todos os clientes quando não há nenhum cadastrado | Base de clientes vazia | Mensagem "Nenhum cliente cadastrado." é exibida | Manual (UI) |

---

## Módulo: Gerenciamento de Produtos — Roupas e Calçados (UC02 / RF02, RF04)

| ID | Descrição | Entrada | Resultado Esperado | Automatizado? |
|----|-----------|---------|---------------------|----------------|
| CT08 | Incluir roupa e consultar pelo ID gerado | Nome: "Camiseta Polo", Preço: 89,90, Tamanho: M, Cor: Azul | Roupa criada e localizável pelo ID retornado | Sim |
| CT09 | Incluir calçado e consultar pelo ID gerado | Nome: "Tênis Esportivo", Numeração: 42, Material: Lona | Calçado criado e localizável pelo ID retornado | Sim |
| CT10 | Polimorfismo: `exibirDetalhes()` difere entre Roupa e Calçado | Uma Roupa e um Calçado com o mesmo preço/estoque | Strings retornadas são diferentes; Roupa contém "Tamanho", Calçado contém "Numeração" | Sim |
| CT-manual-03 | Cadastrar produto com preço negativo via interface | Preço: "-50" | Sistema rejeita o valor e solicita novamente, sem cadastrar o produto | Manual (UI) |
| CT-manual-04 | Cadastrar produto com quantidade em estoque não numérica | Quantidade: "abc" | Sistema exibe mensagem de erro "Valor inválido" e não cadastra o produto | Manual (UI) |
| CT-manual-05 | Alterar preço e estoque de um produto existente via interface | Selecionar produto, informar novo preço e nova quantidade | Produto exibido na listagem com os novos valores | Manual (UI) |

---

## Módulo: Registro de Vendas / Pedidos (UC03 / RF03, RF04)

| ID | Descrição | Entrada | Resultado Esperado | Automatizado? |
|----|-----------|---------|---------------------|----------------|
| CT11 | Registrar venda com estoque suficiente | Cliente + 1 Roupa (estoque: 5) | Pedido criado, valor total = preço do item, estoque do produto reduzido em 1 | Sim |
| CT12 | Registrar venda com estoque insuficiente (regra de negócio) | Tentar vender 2 unidades de produto com estoque = 1 | Sistema lança `EstoqueInsuficienteException`; estoque permanece **inalterado** (operação atômica) | Sim |
| CT13 | Cancelar pedido devolve o estoque | Cancelar um pedido já registrado | Estoque dos produtos do pedido é incrementado de volta ao valor original | Sim |
| CT15 | Cálculo do valor total do pedido com múltiplos produtos | 1 Roupa (R$ 99,90) + 1 Calçado (R$ 220,10) no mesmo pedido | `valorTotal` do pedido = R$ 320,00 | Sim |
| CT-manual-06 | Registrar venda sem clientes cadastrados | Base de clientes vazia, tentar registrar venda | Sistema exibe aviso e impede o início do fluxo de venda | Manual (UI) |
| CT-manual-07 | Adicionar múltiplos produtos a um único pedido via interface | Adicionar 3 produtos diferentes ao mesmo pedido, confirmando "Sim" a cada pergunta de continuação | Pedido final contém os 3 produtos e o valor total correto | Manual (UI) |

---

## Módulo: Persistência de Dados (UC04 / RF05)

| ID | Descrição | Entrada | Resultado Esperado | Automatizado? |
|----|-----------|---------|---------------------|----------------|
| CT14 | Dados sobrevivem ao "reinício" da aplicação | Cadastrar cliente, instanciar novamente o `ClienteService` (simulando reabertura do programa) | Cliente cadastrado anteriormente é encontrado pela nova instância, com os mesmos dados | Sim |
| CT-manual-08 | Fechar e reabrir a aplicação real (`java -jar LojaModaApp.jar`) | Cadastrar clientes/produtos/pedidos, fechar o programa pelo menu "Sair", reabrir | Todos os dados cadastrados anteriormente continuam disponíveis nos menus de listagem | Manual (E2E) |
| CT-manual-09 | Primeira execução sem arquivos de dados existentes | Apagar a pasta `dados/` e iniciar a aplicação | Sistema inicia normalmente com listas vazias, sem lançar exceções | Manual (E2E) |

---

## Módulo: Interface Gráfica (RF06)

| ID | Descrição | Entrada | Resultado Esperado | Automatizado? |
|----|-----------|---------|---------------------|----------------|
| CT-manual-10 | Navegação completa pelo menu principal | Selecionar cada uma das opções do menu principal (Clientes, Produtos, Vendas, Sair) | Cada opção abre o submenu correspondente via `JOptionPane`; "Sair" encerra a aplicação com mensagem de despedida | Manual (UI) |
| CT-manual-11 | Cancelamento em qualquer caixa de diálogo de entrada (botão "Cancelar" ou tecla Esc) | Cancelar em qualquer ponto de um fluxo de cadastro/alteração/exclusão | Operação é abortada de forma segura, sem lançar exceções não tratadas e sem corromper os dados | Manual (UI) |

---

## Como executar os testes automatizados

```bash
# Usando o script de build (compila + executa testes + gera JAR e Javadoc):
./build.sh

# Ou manualmente, após compilar:
java -cp build com.lojamoda.TestesFuncionais
```

A suíte automatizada (`TestesFuncionais.java`) cobre 15 cenários (CT01–CT15)
que validam as regras de negócio centrais do sistema sem depender de
interação manual com o `JOptionPane`. Os cenários marcados como "Manual"
neste documento testam especificamente a camada de interface gráfica e o
comportamento end-to-end da aplicação, devendo ser executados manualmente
pelo avaliador ao rodar `java -jar out/LojaModaApp.jar`.
