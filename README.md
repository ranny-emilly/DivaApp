# Sistema de Gerenciamento do DivaApp

Aplicação Java Desktop para gerenciamento de **Clientes**, **Produtos** (Roupas e Calçados) e **Pedidos de Venda**, com persistência local em arquivos binários e interface gráfica baseada em `JOptionPane`.

Desenvolvido a partir da especificação do trabalho (problema, escopo, requisitos, casos de uso e diagrama de classes UML) por **Elisa Correia, Emilly Ranny, Jolie Pavan, Thyago**.

---

# Importante: sobre a compilação deste pacote

O ambiente onde este projeto foi gerado **não possui o JDK completo instalado** (apenas o JRE, sem `javac`, `jar` e `javadoc`, e sem acesso à internet para instalar o JDK).

Por isso, **o código-fonte foi escrito e revisado manualmente com extremo cuidado, mas não pôde ser compilado neste ambiente**.

O `LojaModaApp.jar` entregue contém o **código-fonte completo** (pasta `src/`), pronto para ser compilado em qualquer máquina com JDK 17 ou superior instalado.

Recomenda-se executar `./build.sh` (ou os comandos equivalentes) antes da entrega final para confirmar a compilação e a execução dos testes.

---

# Estrutura do projeto

```text
─DivaApp
    ├───.idea
    ├───build
    │   └───com
    │       └───lojamoda
    │           ├───exception
    │           ├───model
    │           ├───persistence
    │           ├───service
    │           └───ui
    ├───dados
    ├───docs
    │   ├───javadoc
    │   └───javadoc-preview
    ├───out
    ├───src
    │   ├───main
    │   │   └───java
    │   │       └───com
    │   │           └───lojamoda
    │   │               ├───exception
    │   │               ├───model
    │   │               ├───persistence
    │   │               ├───service
    │   │               └───ui
    │   └───test
    │       └───java
    │           └───com
    │               └───lojamoda
    └───target
        ├───classes
        │   └───com
        │       └───lojamoda
        │           ├───exception
        │           ├───model
        │           ├───persistence
        │           ├───service
        │           └───ui
        └───test-classes
            └───com
                └───lojamoda
```

---

# Mapeamento dos itens solicitados

| Item | Descrição | Onde está implementado |
|------|-----------|------------------------|
| **f** | Classes do diagrama com atributos, métodos e construtores | `model/Produto.java`, `model/Roupa.java`, `model/Cliente.java`, `model/Pedido.java` (fiéis ao diagrama UML do documento) |
| **g** | IDs automáticos e sequenciais via atributos estáticos | Atributo `private static int contadorId` em `Produto`, `Cliente` e `Pedido`, incrementado no construtor (`this.id = contadorId++`) |
| **h** | Armazenamento em arquivos (leitura/gravação de objetos) | `persistence/ArquivoDAO.java`, usando serialização binária Java (`ObjectOutputStream`/`ObjectInputStream`). Arquivos gerados em `dados/clientes.dat`, `dados/produtos.dat` e `dados/pedidos.dat`. |
| **i** | Classe abstrata e/ou interface no modelo | `Produto` é uma classe abstrata com o método abstrato `exibirDetalhes()`, implementado de forma diferente em `Roupa` e `Calcado` (polimorfismo). |
| **j** | CRUD completo (Inclusão, Exclusão, Alteração, Consulta e Lista) | `ClienteService`, `ProdutoService` e `PedidoService` — métodos `incluir`, `incluirRoupa`, `incluirCalcado`, `excluir`, `alterar`, `consultar` e `listar`. |
| **k** | Interface gráfica via `JOptionPane` | `ui/ClienteUI.java`, `ui/ProdutoUI.java`, `ui/PedidoUI.java` e `Main.java` — menus e formulários utilizando `JOptionPane`, sem leitura via console ou `Scanner`. |
| **l** | Casos de teste funcionais | `docs/CASOS_DE_TESTE.md` (15 casos automatizados e 11 casos manuais de interface) e `src/test/java/com/lojamoda/TestesFuncionais.java`. |
| **m** | Tratamento de exceções e exceção customizada | `EstoqueInsuficienteException`, `EntidadeNaoEncontradaException` e `CpfInvalidoException`, tratadas nas camadas de interface com `try/catch`. |
| **n** | Uso de listas/coleções | `ArrayList<Cliente>`, `ArrayList<Produto>` e `ArrayList<Pedido>` nos serviços, além de `ArrayList<Produto>` na classe `Pedido`. |
| **o** | Documentação Javadoc | Todas as classes e métodos possuem documentação no padrão Javadoc. A documentação HTML pode ser gerada com `./build.sh` ou `mvn javadoc:javadoc`. |
| **p** | JAR da aplicação | Gerado por `./build.sh` em `out/LojaModaApp.jar` ou por `mvn package`, gerando `target/LojaModaApp.jar`. |

---

# Onde os dados são salvos

A aplicação cria automaticamente uma pasta `dados/` (relativa ao diretório onde o `.jar` é executado), contendo os arquivos:

- `clientes.dat`
- `produtos.dat`
- `pedidos.dat`

Esses arquivos são lidos automaticamente ao iniciar a aplicação e regravados após cada operação de inclusão, alteração ou exclusão, garantindo a persistência dos dados.

---

