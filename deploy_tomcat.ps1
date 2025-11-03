# ===============================
# deploy_tomcat.ps1
# ===============================
Write-Host "🚀 Iniciando deploy no Tomcat 11..."

# Caminho do artefato gerado pelo Gradle
$sourceWar = "build\libs\blogqateste.war"

# Caminho do diretório webapps do Tomcat
$tomcatDir = "C:\apache-tomcat-11.0.11\webapps"

# Verifica se o arquivo WAR existe
if (!(Test-Path $sourceWar)) {
    Write-Host "❌ Arquivo WAR não encontrado em: $sourceWar"
    exit 1
}

# Para o Tomcat (se estiver em execução)
Write-Host "🛑 Parando Tomcat..."
try {
    Stop-Process -Name "java" -Force -ErrorAction Stop
    Write-Host "✅ Tomcat parado com sucesso."
} catch {
    Write-Host "⚠️ Nenhum processo do Tomcat em execução."
}

# Remove WAR antigo e pasta expandida (se existirem)
Write-Host "🧹 Limpando versão anterior..."
Remove-Item "$tomcatDir\blogqateste.war" -Force -ErrorAction SilentlyContinue
Remove-Item "$tomcatDir\blogqateste" -Recurse -Force -ErrorAction SilentlyContinue

# Copia o novo WAR
Write-Host "📦 Copiando novo WAR para o Tomcat..."
Copy-Item $sourceWar -Destination "$tomcatDir\blogqateste.war"

# Inicia o Tomcat novamente
Write-Host "▶️ Iniciando o Tomcat..."
Start-Process "C:\apache-tomcat-11.0.11\bin\startup.bat"

# Aguarda alguns segundos
Start-Sleep -Seconds 8

Write-Host "✅ Deploy concluído com sucesso!"
Write-Host "🌐 Acesse: http://localhost:9999/blogqateste"
