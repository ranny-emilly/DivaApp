# Sistema de Gerenciamento de Loja de Moda

Aplicação Java Desktop para gerenciamento de **Clientes**, **Produtos**
(Roupas e Calçados) e **Pedidos de Venda**, com persistência local em
arquivos binários e interface gráfica baseada em `JOptionPane`.

Desenvolvido a partir da especificação do trabalho (problema, escopo,
requisitos, casos de uso e diagrama de classes UML) por **Elisa Correia,
Emilly Ranny e Jolie Pavan**.

---

## ⚠️ Importante: sobre a compilação deste pacote

O ambiente onde este projeto foi gerado **não possui o JDK completo
instalado** (apenas o JRE, sem `javac`/`jar`/`javadoc`, e sem acesso à
internet para instalar o JDK). Por isso, **o código-fonte foi escrito e
revisado manualmente com extremo cuidado, mas não pôde ser compilado
neste ambiente**.

O `LojaModaApp.jar` entregue contém o **código-fonte completo** (pasta
`src/`), pronto para ser compilado com um único comando, em qualquer
máquina com JDK 17 ou superior instalado. Veja a seção
[Como compilar e executar](#como-compilar-e-executar) abaixo.

Recomendo fortemente rodar `./build.sh` (ou os comandos manuais
equivalentes) na sua máquina antes da entrega final, para confirmar a
compilação e ver os testes automatizados passando.

---

## Estrutura do projeto

```
LojaModaApp/
├── pom.xml                          # Build via Maven (opcional)
├── build.sh                         # Build via JDK puro - Linux/macOS (javac/javadoc/jar)
├── build.ps1                        # Build via JDK puro - Windows/PowerShell
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

---

## Como compilar e executar

### Opção 1 — Script automático (recomendado, sem Maven)

Requer apenas o **JDK 17+** instalado (não confundir com JRE) — baixe em
[adoptium.net](https://adoptium.net) caso não tenha.

**No Windows (PowerShell):**

```powershell
cd LojaModaApp

# Se o PowerShell bloquear a execução de scripts, rode uma vez antes:
Set-ExecutionPolicy -Scope Process -ExecutionPolicy Bypass

.\build.ps1
```

**No Linux ou macOS (bash):**

```bash
cd LojaModaApp
chmod +x build.sh      # caso a permissão de execução não venha marcada
./build.sh
```

O script faz, em sequência:
1. Compila todas as classes (`src/main/java`) em `build/`.
2. Compila e **executa automaticamente** a suíte de testes funcionais.
3. Gera o JAR executável em `out/LojaModaApp.jar`.
4. Gera a documentação Javadoc em `docs/javadoc/index.html`.

Para executar a aplicação depois do build (mesmo comando nos dois sistemas):

```
java -jar out/LojaModaApp.jar
```

> No PowerShell, se preferir, pode usar barra normal ou invertida no caminho:
> `java -jar out\LojaModaApp.jar` funciona igual.

### Opção 2 — Maven

Funciona igual em Windows, Linux ou macOS, desde que o Maven esteja instalado
e configurado no PATH:

```
cd LojaModaApp
mvn clean package        # compila e gera target/LojaModaApp.jar
mvn javadoc:javadoc       # gera a documentação em target/site/apidocs/index.html
java -jar target/LojaModaApp.jar
```

### Opção 3 — Comandos manuais (passo a passo)

**No Windows (PowerShell):**

```powershell
# 1. Compilar
Get-ChildItem -Path "src\main\java" -Recurse -Filter "*.java" |
    Select-Object -ExpandProperty FullName |
    Set-Content -Path sources.txt -Encoding UTF8
javac -encoding UTF-8 -d build "@sources.txt"

# 2. Executar a aplicação
java -cp build com.lojamoda.Main

# 3. (Opcional) Executar os testes
Get-ChildItem -Path "src\test\java" -Recurse -Filter "*.java" |
    Select-Object -ExpandProperty FullName |
    Set-Content -Path sources_test.txt -Encoding UTF8
javac -encoding UTF-8 -cp build -d build "@sources_test.txt"
java -cp build com.lojamoda.TestesFuncionais

# 4. (Opcional) Gerar o JAR
"Main-Class: com.lojamoda.Main" | Set-Content -Path manifest.txt -Encoding ASCII
jar cfm LojaModaApp.jar manifest.txt -C build com
java -jar LojaModaApp.jar

# 5. (Opcional) Gerar o Javadoc
javadoc -encoding UTF-8 -d docs\javadoc -private "@sources.txt"
```

**No Linux ou macOS (bash):**

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

# 4. (Opcional) Gerar o JAR
echo "Main-Class: com.lojamoda.Main" > manifest.txt
jar cfm LojaModaApp.jar manifest.txt -C build com
java -jar LojaModaApp.jar

# 5. (Opcional) Gerar o Javadoc
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

---

## Observação sobre o RNF02 (banco de dados relacional)

O documento de especificação lista, como requisito não funcional,
**RNF02: O sistema deve utilizar bancos de dados relacionais**. A
implementação entregue atende ao requisito funcional de persistência
(RF05) através de **arquivos binários locais via serialização Java**
(itens **h** e **n** do enunciado pedem explicitamente "uso de
arquivos... como meio de armazenamento" e "Listas... para manipular os
dados lidos e gravados nos arquivos"), o que é compatível com o escopo
descrito no documento ("sem depender de sistemas complexos de banco de
dados em nuvem em fases iniciais de digitalização").

Se for necessário também satisfazer o RNF02 literalmente com um SGBD
relacional (ex: SQLite, H2, MySQL via JDBC), recomendo tratar isso como
uma evolução natural do projeto: a camada `ArquivoDAO` pode ser
substituída por uma camada `*JdbcDAO` equivalente sem qualquer alteração
nas classes de modelo ou de serviço, já que a aplicação foi desenhada em
camadas (model / service / persistence / ui) exatamente para permitir
essa troca.
