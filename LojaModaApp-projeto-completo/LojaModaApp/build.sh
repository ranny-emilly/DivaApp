set -e

echo "==================================================="
echo " Build - Sistema de Gerenciamento de Loja de Moda"
echo "==================================================="

if ! command -v javac &> /dev/null; then
    echo "ERRO: 'javac' nao encontrado. Instale o JDK (ex: sudo apt install openjdk-21-jdk) e tente novamente."
    exit 1
fi

echo "[1/5] Limpando build anterior..."
rm -rf build out docs/javadoc
mkdir -p build out docs/javadoc

echo "[2/5] Compilando classes (src/main/java)..."
find src/main/java -name "*.java" > sources_main.txt
javac -encoding UTF-8 -d build @sources_main.txt
rm sources_main.txt

echo "[3/5] Compilando e executando testes funcionais (src/test/java)..."
find src/test/java -name "*.java" > sources_test.txt
javac -encoding UTF-8 -cp build -d build @sources_test.txt
rm sources_test.txt
(cd build && java com.lojamoda.TestesFuncionais)

echo "[4/5] Gerando JAR executavel..."
mkdir -p dados
echo "Main-Class: com.lojamoda.Main" > manifest.txt
jar cfm out/LojaModaApp.jar manifest.txt -C build com
rm manifest.txt
echo "JAR gerado em: out/LojaModaApp.jar"

echo "[5/5] Gerando documentacao Javadoc..."
find src/main/java -name "*.java" > sources_javadoc.txt
javadoc -encoding UTF-8 -docencoding UTF-8 -charset UTF-8 \
    -d docs/javadoc \
    -private \
    -doctitle "Sistema de Gerenciamento de Loja de Moda - Documentacao da API" \
    -windowtitle "Loja de Moda - Javadoc" \
    @sources_javadoc.txt
rm sources_javadoc.txt
echo "Javadoc gerado em: docs/javadoc/index.html"

echo ""
echo "==================================================="
echo " Build concluido com sucesso!"
echo " Para executar a aplicacao: java -jar out/LojaModaApp.jar"
echo "==================================================="
