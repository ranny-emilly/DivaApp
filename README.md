# Sistema de Gerenciamento de Loja de Moda

Aplicação Java Desktop para gerenciamento de **Clientes**, **Produtos**
(Roupas e Calçados) e **Pedidos de Venda**, com persistência local em
arquivos binários e interface gráfica baseada em `JOptionPane`.

Desenvolvido a partir da especificação do trabalho (problema, escopo,
requisitos, casos de uso e diagrama de classes UML) por **Elisa Correia,
Emilly Ranny, Jolie Pavan e Thyago Divino**.

---

## Importante: sobre a compilação deste pacote

O `LojaModaApp.jar` entregue contém o **código-fonte completo** (pasta
`src/`), pronto para ser compilado com um único comando, em qualquer
máquina com JDK 17 ou superior instalado.

---

## Estrutura do projeto

```
LojaModaApp/
├── pom.xml                          # Build via Maven (opcional)
├── build.sh                         # Build via JDK puro (javac/javadoc/jar)
├── src/
│   ├── main/java/com/lojamoda/
│   │   ├── Main.java                # Ponto de entrada da aplicação
│   │   ├── model/                   # Classes de domínio (f, g, i)
│   │   │   ├── Produto.java         # Classe ABSTRATA
│   │   │   ├── Roupa.java           # Herda de Produto
│   │   │   ├── Calcado.java         # Herda de Produto
│   │   │   ├── Cliente.java
│   │   │   └── Pedido.java
│   │   ├── exception/                # Exceções customizadas (m)
│   │   │   ├── EstoqueInsuficienteException.java
│   │   │   ├── EntidadeNaoEncontradaException.java
│   │   │   └── CpfInvalidoException.java
│   │   ├── persistence/
│   │   │   └── ArquivoDAO.java      # Leitura/gravação em arquivo (h, n)
│   │   ├── service/                  # Regras de negócio + CRUD (j)
│   │   │   ├── ClienteService.java
│   │   │   ├── ProdutoService.java
│   │   │   └── PedidoService.java
│   │   └── ui/                       # Interface JOptionPane (k)
│   │       ├── ClienteUI.java
│   │       ├── ProdutoUI.java
│   │       └── PedidoUI.java
│   └── test/java/com/lojamoda/
│       └── TestesFuncionais.java     # Casos de teste automatizados (l)
└── docs/
    └── CASOS_DE_TESTE.md             # Casos de teste funcionais documentados (l)
```

---

## Mapeamento dos itens solicitados

| Item | Descrição | Onde está implementado |
|------|-----------|--------------------------|
| **f** | Classes do diagrama com atributos, métodos e construtores | `model/Produto.java`, `model/Roupa.java`, `model/Cliente.java`, `model/Pedido.java` (fiéis ao diagrama UML do documento) |
| **g** | IDs automáticos e sequenciais via atributos estáticos | Atributo `private static int contadorId` em `Produto`, `Cliente` e `Pedido`, incrementado no construtor (`this.id = contadorId++`) |
| **h** | Armazenamento em arquivos (leitura/gravação de objetos) | `persistence/ArquivoDAO.java`, usando serialização binária Java (`ObjectOutputStream`/`ObjectInputStream`). Arquivos gerados em `dados/clientes.dat`, `dados/produtos.dat`, `dados/pedidos.dat` |
| **i** | Classe abstrata e/ou interface no modelo | `Produto` é uma classe **abstrata** com o método abstrato `exibirDetalhes()`, implementado de forma diferente em `Roupa` e `Calcado` (polimorfismo) |
| **j** | CRUD completo (Inclusão, Exclusão, Alteração, Consulta, Lista) | `ClienteService`, `ProdutoService` e `PedidoService` — métodos `incluir`/`incluirRoupa`/`incluirCalcado`, `excluir`, `alterar`, `consultar`, `listar` |
| **k** | Interface gráfica via JOptionPane | `ui/ClienteUI.java`, `ui/ProdutoUI.java`, `ui/PedidoUI.java` e `Main.java` — menus e formulários 100% em `JOptionPane` (nenhuma leitura via console/`Scanner`) |
| **l** | Casos de teste funcionais | `docs/CASOS_DE_TESTE.md` (documento com 15 casos automatizados + 11 casos manuais de UI) e `src/test/java/com/lojamoda/TestesFuncionais.java` (suíte executável) |
| **m** | Tratamento de exceções + exceção customizada de negócio | `EstoqueInsuficienteException` (regra: não é possível vender mais do que há em estoque), `EntidadeNaoEncontradaException` e `CpfInvalidoException`. Tratadas em todas as camadas de UI com `try/catch` e mensagens amigáveis |
| **n** | Uso de Listas/Coleções | `ArrayList<Cliente>`, `ArrayList<Produto>`, `ArrayList<Pedido>` em todos os serviços; `ArrayList<Produto>` dentro de `Pedido` (agregação, conforme diagrama) |
| **o** | Documentação Javadoc | Todas as classes/métodos possuem comentários `/** ... */` no padrão Javadoc. Gerar a página HTML com `./build.sh` (ou `mvn javadoc:javadoc`) |
| **p** | JAR da aplicação | Gerado por `./build.sh` em `out/LojaModaApp.jar` (ou `mvn package`, gerando `target/LojaModaApp.jar`) |


### Comandos manuais (passo a passo)

```bash
# 1. Compilar
find src/main/java -name "*.java" > sources.txt
javac -encoding UTF-8 -d build @sources.txt

# 2. Executar a aplicação
java -cp build com.lojamoda.Main

# 3. (Opcional) Executar os testes
find src/test/java -name "*.java" > sources_test.txt
javac -encoding UTF-8 -cp build -d build @sources_test.txt
java -cp build com.lojamoda.TestesFuncionais

# 4. Gerar o JAR
echo "Main-Class: com.lojamoda.Main" > manifest.txt
jar cfm LojaModaApp.jar manifest.txt -C build com
java -jar LojaModaApp.jar

# 5. Gerar o Javadoc
javadoc -encoding UTF-8 -d docs/javadoc -private @sources.txt
```

---

## Onde os dados são salvos

A aplicação cria automaticamente uma pasta `dados/` (relativa ao
diretório de onde o `.jar` é executado), contendo:

- `clientes.dat`
- `produtos.dat`
- `pedidos.dat`

Esses arquivos são lidos automaticamente ao iniciar a aplicação e
regravados após cada operação de Inclusão, Alteração ou Exclusão,
garantindo que nada se perca ao fechar o programa (RF05).


