# =============================================================================
# Script de build do Sistema de Gerenciamento de Loja de Moda (Windows)
# Compila o projeto, gera o JAR executavel e a documentacao Javadoc,
# usando apenas as ferramentas padrao do JDK (sem necessidade de Maven).
#
# Pre-requisito: JDK 17+ instalado, com javac/javadoc/jar no PATH.
# Uso (no PowerShell, dentro da pasta LojaModaApp):
#   .\build.ps1
#
# Se o PowerShell bloquear a execucao de scripts, rode antes (uma vez):
#   Set-ExecutionPolicy -Scope Process -ExecutionPolicy Bypass
# =============================================================================

$ErrorActionPreference = "Stop"

Write-Host "==================================================="
Write-Host " Build - Sistema de Gerenciamento de Loja de Moda"
Write-Host "==================================================="

# Verifica se o javac esta disponivel
if (-not (Get-Command javac -ErrorAction SilentlyContinue)) {
    Write-Host "ERRO: 'javac' nao encontrado no PATH." -ForegroundColor Red
    Write-Host "Instale o JDK (ex: https://adoptium.net) e garanta que 'javac' funcione no terminal." -ForegroundColor Red
    exit 1
}

# Limpa builds anteriores
Write-Host "[1/5] Limpando build anterior..."
Remove-Item -Recurse -Force -ErrorAction SilentlyContinue build, out, "docs\javadoc"
New-Item -ItemType Directory -Force -Path build, out, "docs\javadoc" | Out-Null

# Compila as classes principais
Write-Host "[2/5] Compilando classes (src\main\java)..."
$sourcesMain = Get-ChildItem -Path "src\main\java" -Recurse -Filter "*.java" | Select-Object -ExpandProperty FullName
$sourcesMain | Set-Content -Path "sources_main.txt" -Encoding UTF8
javac -encoding UTF-8 -d build "@sources_main.txt"
Remove-Item "sources_main.txt"

# Compila e executa os testes funcionais
Write-Host "[3/5] Compilando e executando testes funcionais (src\test\java)..."
$sourcesTest = Get-ChildItem -Path "src\test\java" -Recurse -Filter "*.java" | Select-Object -ExpandProperty FullName
$sourcesTest | Set-Content -Path "sources_test.txt" -Encoding UTF8
javac -encoding UTF-8 -cp build -d build "@sources_test.txt"
Remove-Item "sources_test.txt"
Push-Location build
java com.lojamoda.TestesFuncionais
Pop-Location

# Gera o JAR executavel
Write-Host "[4/5] Gerando JAR executavel..."
New-Item -ItemType Directory -Force -Path dados | Out-Null
"Main-Class: com.lojamoda.Main" | Set-Content -Path "manifest.txt" -Encoding ASCII
jar cfm "out\LojaModaApp.jar" manifest.txt -C build com
Remove-Item "manifest.txt"
Write-Host "JAR gerado em: out\LojaModaApp.jar"

# Gera a documentacao Javadoc
Write-Host "[5/5] Gerando documentacao Javadoc..."
$sourcesJavadoc = Get-ChildItem -Path "src\main\java" -Recurse -Filter "*.java" | Select-Object -ExpandProperty FullName
$sourcesJavadoc | Set-Content -Path "sources_javadoc.txt" -Encoding UTF8
javadoc -encoding UTF-8 -docencoding UTF-8 -charset UTF-8 `
    -d "docs\javadoc" `
    -private `
    -doctitle "Sistema de Gerenciamento de Loja de Moda - Documentacao da API" `
    -windowtitle "Loja de Moda - Javadoc" `
    "@sources_javadoc.txt"
Remove-Item "sources_javadoc.txt"
Write-Host "Javadoc gerado em: docs\javadoc\index.html"

Write-Host ""
Write-Host "==================================================="
Write-Host " Build concluido com sucesso!"
Write-Host " Para executar a aplicacao: java -jar out\LojaModaApp.jar"
Write-Host "==================================================="
