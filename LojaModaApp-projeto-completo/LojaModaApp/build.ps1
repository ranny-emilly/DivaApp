$ErrorActionPreference = "Stop"

Write-Host " Build - Sistema de Gerenciamento de Loja de Moda"

if (-not (Get-Command javac -ErrorAction SilentlyContinue)) {
    Write-Host "ERRO: 'javac' nao encontrado no PATH." -ForegroundColor Red
    Write-Host "Instale o JDK (ex: https://adoptium.net) e garanta que 'javac' funcione no terminal." -ForegroundColor Red
    exit 1
}

Write-Host "[1/5] Limpando build anterior..."
Remove-Item -Recurse -Force -ErrorAction SilentlyContinue build, out, "docs\javadoc"
New-Item -ItemType Directory -Force -Path build, out, "docs\javadoc" | Out-Null

Write-Host "[2/5] Compilando classes (src\main\java)..."
$sourcesMain = Get-ChildItem -Path "src\main\java" -Recurse -Filter "*.java" | Select-Object -ExpandProperty FullName
$sourcesMain | Set-Content -Path "sources_main.txt" -Encoding UTF8
javac -encoding UTF-8 -d build "@sources_main.txt"
Remove-Item "sources_main.txt"

Write-Host "[3/5] Compilando e executando testes funcionais (src\test\java)..."
$sourcesTest = Get-ChildItem -Path "src\test\java" -Recurse -Filter "*.java" | Select-Object -ExpandProperty FullName
$sourcesTest | Set-Content -Path "sources_test.txt" -Encoding UTF8
javac -encoding UTF-8 -cp build -d build "@sources_test.txt"
Remove-Item "sources_test.txt"
Push-Location build
java com.lojamoda.TestesFuncionais
Pop-Location

Write-Host "[4/5] Gerando JAR executavel..."
New-Item -ItemType Directory -Force -Path dados | Out-Null
"Main-Class: com.lojamoda.Main" | Set-Content -Path "manifest.txt" -Encoding ASCII
jar cfm "out\LojaModaApp.jar" manifest.txt -C build com
Remove-Item "manifest.txt"
Write-Host "JAR gerado em: out\LojaModaApp.jar"

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
Write-Host " Build concluido com sucesso!"
Write-Host " Para executar a aplicacao: java -jar out\LojaModaApp.jar"
