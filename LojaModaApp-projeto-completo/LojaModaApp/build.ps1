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

function Invoke-Tool {
    param(
        [Parameter(Mandatory = $true)]
        [string] $Command,

        [Parameter(Mandatory = $true)]
        [string[]] $Arguments
    )

    & $Command @Arguments
    if ($LASTEXITCODE -ne 0) {
        throw "Comando falhou com codigo ${LASTEXITCODE}: $Command $($Arguments -join ' ')"
    }
}

function Get-JavaSources {
    param(
        [Parameter(Mandatory = $true)]
        [string] $Path
    )

    $sources = @(Get-ChildItem -Path $Path -Recurse -Filter "*.java" | Sort-Object FullName | ForEach-Object { $_.FullName })
    if ($sources.Count -eq 0) {
        throw "Nenhum arquivo .java encontrado em '$Path'."
    }

    return $sources
}

Write-Host "==================================================="
Write-Host " Build - Sistema de Gerenciamento de Loja de Moda"
Write-Host "==================================================="

# Verifica se as ferramentas do JDK estao disponiveis
foreach ($tool in @("javac", "java", "jar", "javadoc")) {
    if (-not (Get-Command $tool -ErrorAction SilentlyContinue)) {
        Write-Host "ERRO: '$tool' nao encontrado no PATH." -ForegroundColor Red
        Write-Host "Instale o JDK (ex: https://adoptium.net) e garanta que '$tool' funcione no terminal." -ForegroundColor Red
        exit 1
    }
}

# Limpa builds anteriores
Write-Host "[1/5] Limpando build anterior..."
Remove-Item -Recurse -Force -ErrorAction SilentlyContinue build, out, "docs\javadoc"
New-Item -ItemType Directory -Force -Path build, out, "docs\javadoc" | Out-Null

# Compila as classes principais
Write-Host "[2/5] Compilando classes (src\main\java)..."
$sourcesMain = Get-JavaSources "src\main\java"
$javacMainArgs = @("-encoding", "UTF-8", "-d", "build") + $sourcesMain
Invoke-Tool "javac" $javacMainArgs

# Compila e executa os testes funcionais
Write-Host "[3/5] Compilando e executando testes funcionais (src\test\java)..."
$sourcesTest = Get-JavaSources "src\test\java"
$javacTestArgs = @("-encoding", "UTF-8", "-cp", "build", "-d", "build") + $sourcesTest
Invoke-Tool "javac" $javacTestArgs
Invoke-Tool "java" @("-cp", "build", "com.lojamoda.TestesFuncionais")

# Gera o JAR executavel
Write-Host "[4/5] Gerando JAR executavel..."
New-Item -ItemType Directory -Force -Path dados | Out-Null
"Main-Class: com.lojamoda.Main" | Set-Content -Path "manifest.txt" -Encoding ASCII
Invoke-Tool "jar" @("cfm", "out\LojaModaApp.jar", "manifest.txt", "-C", "build", "com")
Remove-Item "manifest.txt"
Write-Host "JAR gerado em: out\LojaModaApp.jar"

# Gera a documentacao Javadoc
Write-Host "[5/5] Gerando documentacao Javadoc..."
$javadocArgs = @(
    "-encoding", "UTF-8",
    "-docencoding", "UTF-8",
    "-charset", "UTF-8",
    "-d", "docs\javadoc",
    "-private",
    "-doctitle", "Sistema de Gerenciamento de Loja de Moda - Documentacao da API",
    "-windowtitle", "Loja de Moda - Javadoc"
) + $sourcesMain
Invoke-Tool "javadoc" $javadocArgs
Write-Host "Javadoc gerado em: docs\javadoc\index.html"

Write-Host ""
Write-Host "==================================================="
Write-Host " Build concluido com sucesso!"
Write-Host " Para executar a aplicacao: java -jar out\LojaModaApp.jar"
Write-Host "==================================================="
