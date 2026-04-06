# Script para limpiar instancias de Java antes de arrancar TFGFitApp
# Uso: .\cleanup-java.ps1

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "   Limpiador de instancias Java" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# 1. Verificar instancias actuales
$javaProcesses = Get-Process -Name java -ErrorAction SilentlyContinue

if ($javaProcesses) {
    Write-Host "⚠️  Encontradas $($javaProcesses.Count) instancia(s) de Java corriendo:" -ForegroundColor Yellow
    $javaProcesses | Select-Object Id, @{Name="Memory(MB)";Expression={[math]::Round($_.WorkingSet64 / 1MB, 2)}}, StartTime | Format-Table -AutoSize

    Write-Host ""
    $confirm = Read-Host "¿Deseas matar TODAS las instancias de Java? (S/N)"

    if ($confirm -eq 'S' -or $confirm -eq 's') {
        Write-Host "Matando procesos Java..." -ForegroundColor Yellow
        Stop-Process -Name java -Force -ErrorAction SilentlyContinue
        Start-Sleep -Seconds 2

        $remaining = Get-Process -Name java -ErrorAction SilentlyContinue
        if ($remaining) {
            Write-Host "❌ ERROR: Aún quedan $($remaining.Count) instancia(s)" -ForegroundColor Red
            $remaining | Format-Table -AutoSize
            exit 1
        } else {
            Write-Host "✅ Todas las instancias eliminadas correctamente" -ForegroundColor Green
        }
    } else {
        Write-Host "⏭️  Cancelado por el usuario" -ForegroundColor Gray
        exit 0
    }
} else {
    Write-Host "✅ No hay instancias de Java corriendo" -ForegroundColor Green
}

Write-Host ""

# 2. Verificar puerto 8081
Write-Host "Verificando puerto 8081..." -ForegroundColor Cyan
$portCheck = netstat -ano | Select-String ":8081"

if ($portCheck) {
    Write-Host "❌ Puerto 8081 AÚN OCUPADO por:" -ForegroundColor Red
    $portCheck

    # Intentar identificar el PID
    $portCheck -match "LISTENING\s+(\d+)" | Out-Null
    $pid = $matches[1]

    if ($pid) {
        Write-Host ""
        $killPort = Read-Host "¿Deseas matar el proceso (PID: $pid)? (S/N)"
        if ($killPort -eq 'S' -or $killPort -eq 's') {
            Stop-Process -Id $pid -Force -ErrorAction SilentlyContinue
            Start-Sleep -Seconds 2
            Write-Host "✅ Proceso eliminado" -ForegroundColor Green
        }
    }
} else {
    Write-Host "✅ Puerto 8081 está LIBRE" -ForegroundColor Green
}

Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "   Listo para arrancar TFGFitApp" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "Puedes arrancar la aplicación desde IntelliJ IDEA o con:" -ForegroundColor Gray
Write-Host "  .\mvnw.cmd spring-boot:run" -ForegroundColor White
Write-Host ""

